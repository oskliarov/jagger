package com.griddynamics.jagger.test.jaas.invoker;


import com.griddynamics.jagger.invoker.v2.DefaultHttpInvoker;
import com.griddynamics.jagger.invoker.v2.SpringBasedHttpClient;

import java.util.HashMap;
import java.util.Map;

public class InvokerWithoutStatusCodeValidation extends DefaultHttpInvoker {
    public InvokerWithoutStatusCodeValidation() {
        super();
        Map<String, Object> params = new HashMap<>();
        params.put(SpringBasedHttpClient.JSpringBasedHttpClientParameters.ERROR_HANDLER.getValue(), new ResponseHandler());
        this.httpClient = new SpringBasedHttpClient(params);
    }
}
