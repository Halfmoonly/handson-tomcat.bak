package com.minit.startup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLClassLoader;

import com.minit.Logger;
import com.minit.connector.http.HttpConnector;
import com.minit.core.ContainerListenerDef;
import com.minit.core.FilterDef;
import com.minit.core.FilterMap;
import com.minit.core.StandardContext;
import com.minit.core.WebappClassLoader;
import com.minit.logger.FileLogger;

public class BootStrap {
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "webroot";
    private static int debug = 0;

    public static void main(String[] args) {
        if (debug >= 1)
            log(".... startup ....");

        System.setProperty("minit.base", WEB_ROOT);

        HttpConnector connector = new HttpConnector();
        StandardContext container = new StandardContext();

        container.setPath("/app1");
        container.setDocBase("app1");
        WebappClassLoader loader = new WebappClassLoader();
        container.setLoader(loader);
        loader.start();

        connector.setContainer(container);
        container.setConnector(connector);

        container.start();
        connector.start();
    }
    private static void log(String message) {
        System.out.print("Bootstrap: ");
        System.out.println(message);
    }

    private static void log(String message, Throwable exception) {
        log(message);
        exception.printStackTrace(System.out);

    }

}