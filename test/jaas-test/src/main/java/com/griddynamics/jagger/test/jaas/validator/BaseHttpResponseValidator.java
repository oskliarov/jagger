package com.griddynamics.jagger.test.jaas.validator;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.HttpRelated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseHttpResponseValidator<Q, E> extends ResponseValidator<Q, E, JHttpResponse> implements HttpRelated {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpResponseValidator.class);

    public BaseHttpResponseValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "BaseHttpResponseValidator";
    }

    @Override
    public abstract boolean validate(Q query, E endpoint, JHttpResponse result, long duration);

    protected void logResponseAsFailed(Q query, E endpoint, JHttpResponse response, String message){
        LOGGER.warn("{}'s query response content is not valid, due to [{}].", query.toString(), message);
        LOGGER.warn(String.format
                ("------> Failed response:\n%s \n%s",
                        endpoint.toString(), response.toString()));
    }
}