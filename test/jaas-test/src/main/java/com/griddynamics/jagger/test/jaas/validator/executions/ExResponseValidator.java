package com.griddynamics.jagger.test.jaas.validator.executions;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.entity.ExecutionEntity;
import com.griddynamics.jagger.test.jaas.validator.BaseHttpResponseValidator;


public class ExResponseValidator extends BaseHttpResponseValidator {
    public ExResponseValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    protected boolean isValid(JHttpQuery query, JHttpEndpoint endpoint, JHttpResponse result) {
        ExecutionEntity actual = (ExecutionEntity) result.getBody();

        String[] parts = query.getPath().split("/");
        ExecutionEntity expected = ExecutionEntity.getDefault();
        expected.setId(Long.parseLong(parts[parts.length - 1]));
        expected.setStatus(ExecutionEntity.TestExecutionStatus.PENDING);

        return expected.equals(actual);
    }
}
