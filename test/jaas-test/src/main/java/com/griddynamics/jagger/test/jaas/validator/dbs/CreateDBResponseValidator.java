package com.griddynamics.jagger.test.jaas.validator.dbs;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.invoker.http.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.validator.BaseHttpResponseValidator;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * Validates response of POST  /jaas/dbs/ .
 * Expected:
 * - Created record's id in the Location Header.
 */
public class CreateDBResponseValidator extends BaseHttpResponseValidator<JHttpQuery<String>, JHttpEndpoint> {

    public CreateDBResponseValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "CreateDBResponseValidator";
    }

    @Override
    public boolean validate(JHttpQuery<String> query, JHttpEndpoint endpoint, JHttpResponse result, long duration)  {
        boolean isValid;

        //Checks.
        try {
            String locationHdr = result.getHeaders().getFirst(HDR_LOCATION);
            Assert.assertFalse("Location header is null.", null == locationHdr);
            Assert.assertTrue(locationHdr.length() > query.getPath().length());
            Assert.assertTrue(locationHdr.contains(query.getPath()));

            String[] parts = locationHdr.split("/"); //Get Id from the query path.
            String theLast = parts[parts.length - 1];
            Assert.assertTrue(Integer.parseInt(theLast) > 0);

            TestContext.addCreatedDbConfigId(theLast); //Store it for further clean-up.
            isValid = true;
        } catch (AssertionFailedError | NumberFormatException e) {
            isValid = false;
            logResponseAsFailed(query, endpoint, result, e.getMessage());
        }

        return isValid;
    }
}