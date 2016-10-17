package com.griddynamics.jagger.test.jaas.validator;

import com.griddynamics.jagger.coordinator.NodeContext;

public class ResponseStatus204Validator<Q, E> extends ResponseStatusValidator<Q, E> {

    public ResponseStatus204Validator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "ResponseStatus204Validator";
    }

    protected int getExpectedStatusCode(){ return 204; }
}