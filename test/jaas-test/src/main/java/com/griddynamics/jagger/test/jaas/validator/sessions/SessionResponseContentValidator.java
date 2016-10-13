package com.griddynamics.jagger.test.jaas.validator.sessions;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.engine.e1.services.data.service.SessionEntity;
import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * Validates response of /jaas/sessions/{id}.
 * Expected:
 * - response contains one session record only;
 * - actual session record is the same as expected one.
 */
public class SessionResponseContentValidator<E> extends SessionsListResponseContentValidator<E> {

    public SessionResponseContentValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "SessionResponseContentValidator";
    }

    @Override
    public boolean validate(JHttpQuery<String> query, E endpoint, JHttpResponse result, long duration)  {
        boolean isValid = false;

        //Checks.
        try {
            SessionEntity actualSession = (SessionEntity)result.getBody();

            String[] queryParts = query.getPath().split("/"); //Get SessionId from the query path.
            SessionEntity expectedSession = TestContext.getSession(queryParts[queryParts.length - 1]);

            Assert.assertEquals("Expected and actual session are not equal.", expectedSession, actualSession);
            isValid = true;
        } catch (AssertionFailedError e) {
            isValid = false;
            logResponseAsFailed(query, endpoint, result, e.getMessage());
        }

        return isValid;
    }
}