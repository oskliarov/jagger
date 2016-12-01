package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.coordinator.NodeContext;

public class TrueValidator extends ResponseValidator {

    public TrueValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "TrueValidator";
    }

    @Override
    public boolean validate(Object query, Object endpoint, Object result, long duration) {
        return true;
    }
}
