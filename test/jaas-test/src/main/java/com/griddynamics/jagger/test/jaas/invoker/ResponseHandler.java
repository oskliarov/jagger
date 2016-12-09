package com.griddynamics.jagger.test.jaas.invoker;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.DefaultResponseErrorHandler;


public class ResponseHandler extends DefaultResponseErrorHandler {

    protected boolean hasError(HttpStatus statusCode) {
        return false;
    }

}
