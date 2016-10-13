package com.griddynamics.jagger.test.jaas.validator.dbs;

import com.griddynamics.jagger.coordinator.NodeContext;
import com.griddynamics.jagger.invoker.http.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.http.v2.JHttpResponse;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;
import com.griddynamics.jagger.test.jaas.validator.BaseHttpResponseValidator;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Validates response of /jaas/dbs.
 * Expected:
 * - list of DB configs is of size 1 and greater;
 * - the list contains no duplicates;
 * - the list is no longer than the expected one (to be configured in the env.properties);
 * - a randomly picked record is the same as corresponding expected one.
 */
public class DBsListResponseContentValidator extends BaseHttpResponseValidator<JHttpQuery<String>, JHttpEndpoint> {

    public DBsListResponseContentValidator(String taskId, String sessionId, NodeContext kernelContext) {
        super(taskId, sessionId, kernelContext);
    }

    @Override
    public String getName() {
        return "DBsListResponseContentValidator";
    }

    @Override
    public boolean validate(JHttpQuery<String> query, JHttpEndpoint endpoint, JHttpResponse result, long duration)  {
        List<DbConfigEntity> actualEntities = Arrays.asList((DbConfigEntity[]) result.getBody());

        boolean isValid = false;

        //Checks.
        try {
            int actlSize = actualEntities.size();
            int expctdSize = TestContext.getDbConfigs().size();
            Assert.assertTrue("Several DB config records are expected. Check returned list's size", 1 < actlSize);

            List<DbConfigEntity> noDuplicatesActualList = actualEntities.stream().distinct().collect(Collectors.toList());
            Assert.assertEquals("Response contains duplicate DB config records", actlSize, noDuplicatesActualList.size());

            Assert.assertEquals(String.format("Actual list(%d) and expected one(%d) differ in size.", actlSize, expctdSize), actlSize, expctdSize);

            Assert.assertEquals("Actual set of DB configs is not the same as the expected one.",
                    actualEntities.stream().collect(Collectors.toSet()),
                    TestContext.getDbConfigs());

            DbConfigEntity randomActualEntity = actualEntities.get((new Random().nextInt(actlSize)));
            DbConfigEntity correspondingExpectedEntity = TestContext.getDbConfig(randomActualEntity.getId());

            Assert.assertEquals("Randomly selected expected and actual DB configs are not equal.", correspondingExpectedEntity, randomActualEntity);
            isValid = true;
        } catch (AssertionFailedError e) {
            isValid = false;
            logResponseAsFailed(query, endpoint, result, e.getMessage());
        }

        return isValid;
    }
}