package server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class ServletContainer {
    HttpConnector connector = null;
    ClassLoader loader = null;
    Map<String,String> servletClsMap = new ConcurrentHashMap<>(); //servletName - ServletClassName
    Map<String,Servlet> servletInstanceMap = new ConcurrentHashMap<>();//servletName - servlet

    public ServletContainer() {
        try {
            // create a URLClassLoader
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(HttpServer.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            System.out.println(e.toString() );
        }
    }
    public String getInfo() {
        return null;
    }

    public ClassLoader getLoader(){
        return this.loader;
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

    public HttpConnector getConnector() {
        return connector;
    }
    public void setConnector(HttpConnector connector) {
        this.connector = connector;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
    }

    public void invoke(HttpRequest request, HttpResponse response)
            throws IOException, ServletException {
        Servlet servlet = null;
        ClassLoader loader = getLoader();
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        String servletClassName = servletName;

        servlet = servletInstanceMap.get(servletName);
        if (servlet == null) {
            Class<?> servletClass = null;
            try {
                servletClass = loader.loadClass(servletClassName);
            } catch (ClassNotFoundException e) {
                System.out.println(e.toString());
            }
            try {
                servlet = (Servlet) servletClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            servletClsMap.put(servletName, servletClassName);
            servletInstanceMap.put(servletName, servlet);

            servlet.init(null);
        }

        try {
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            HttpResponseFacade responseFacade = new HttpResponseFacade(response);
            System.out.println("Call service()");
            servlet.service(requestFacade, responseFacade);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        catch (Throwable e) {
            System.out.println(e.toString());
        }
    }
}
