package com.minit.connector.http;

import com.minit.connector.http.HttpConnector;
import com.minit.connector.http.HttpRequestImpl;
import com.minit.connector.http.HttpResponseImpl;

import javax.servlet.ServletException;
import java.io.*;

public class ServletProcessor {
    private HttpConnector connector;

    public ServletProcessor(HttpConnector connector) {
        this.connector = connector;
    }

    public void process(HttpRequestImpl request, HttpResponseImpl response) throws IOException, ServletException {
        this.connector.getContainer().invoke(request, response);
    }

}
