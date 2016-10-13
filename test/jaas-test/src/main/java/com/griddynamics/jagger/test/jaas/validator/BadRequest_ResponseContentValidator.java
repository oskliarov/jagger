package com.griddynamics.jagger.test.jaas.validator;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.invoker.http.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * Validates 400 responses.
 *
 * Expected:
 * - response entity contains some error explanation text.
  */
public class BadRequest_ResponseContentValidator extends BaseHttpResponseValidator<JHttpQuery<String>, JHttpEndpoint> {

    public BadRequest_ResponseContentValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "BadRequest_ResponseContentValidator";
    }

    @Override
    public boolean validate(JHttpQuery<String> query, JHttpEndpoint endpoint, JHttpResponse result, long duration)  {
        boolean isValid = false;

        //Checks.
        try {
            String actualEntity = (String)result.getBody();
            Assert.assertTrue(actualEntity.toLowerCase().contains("error page"));
            Assert.assertTrue(actualEntity.contains("NumberFormatException"));
            isValid = true;
        } catch (AssertionFailedError e) {
            isValid = false;
            logResponseAsFailed(query, endpoint, result, e.getMessage());
        }

        return isValid;
    }
}