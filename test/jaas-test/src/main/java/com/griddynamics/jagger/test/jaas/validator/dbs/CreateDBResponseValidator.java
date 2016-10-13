package com.griddynamics.jagger.test.jaas.validator.dbs;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.invoker.http.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.validator.BaseHttpResponseValidator;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates response of POST  /jaas/dbs/ .
 * Expected:
 * - Created record's id in the Location Header.
 */
public class CreateDBResponseValidator extends BaseHttpResponseValidator<JHttpQuery<String>, JHttpEndpoint> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDBResponseValidator.class);

    public CreateDBResponseValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "CreateDBResponseValidator";
    }

    @Override
    public boolean validate(JHttpQuery<String> query, JHttpEndpoint endpoint, JHttpResponse result, long duration)  {
        boolean isValid = false;

        //Checks.
        try {
            String locationHdr = result.getHeaders().getFirst(HDR_LOCATION);
            Assert.assertFalse("Location header is null.", null == locationHdr);
            Assert.assertTrue(locationHdr.length() > query.getPath().length());
            Assert.assertTrue(locationHdr.contains(query.getPath()));

            String[] parts = locationHdr.split("/"); //Get Id from the query path.
            TestContext.addCreatedDbConfigId(parts[parts.length - 1]); //Store it for further clean-up.
            isValid = true;
        } catch (AssertionFailedError e) {
            isValid = false;
            logResponseAsFailed(query, endpoint, result, e.getMessage());
        }

        return isValid;
    }
}