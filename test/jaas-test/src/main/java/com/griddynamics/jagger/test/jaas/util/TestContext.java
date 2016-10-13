package com.griddynamics.jagger.test.jaas.util;

import com.griddynamics.jagger.engine.e1.services.data.service.SessionEntity;
import com.griddynamics.jagger.engine.e1.services.data.service.TestEntity;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;

import java.util.*;

/**
 * Stores and provides access to test context (expected data mainly).
 * Created by ELozovan on 2016-09-28.
 */
public class TestContext {
    private static volatile TestContext instance;

    private Set<SessionEntity> sessions = new TreeSet<>();
    private Map<String,Set<TestEntity>> tests = new HashMap<>();

    /**
     * To be loaded and used in Read tests for /jaas/dbs resource.
     */
    private Set<DbConfigEntity> dbConfigs = new TreeSet<>();

    /**
     * To be generated and used in Create/Update/Delete tests for /jaas/dbs resource.
     */
    private Set<DbConfigEntity> dbConfigsToCreate = new HashSet<>();

    /**
     * DB Config Ids (Strings) which were created during test run.
     */
    private List<String> createdDbConfigIds = new ArrayList<>();

    private TestContext() {}

    public static TestContext get() {
        TestContext localInstance = instance;
        if (localInstance == null) {
            synchronized (TestContext.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TestContext();
                }
            }
        }
        return localInstance;
    }

    public static Set<SessionEntity> getSessions() {
        return get().sessions;
    }

    /**
     * Returns NULL if no session entity found.
     *
     */
    public static SessionEntity getSession(String id) {
        return get().sessions.stream().filter((s)->id.equals(s.getId())).findFirst().orElse(null);
    }

    public static void setSessions(Set<SessionEntity> sessions) {
        get().sessions = sessions;
    }

    public static Map<String, Set<TestEntity>> getTests() {
        return get().tests;
    }

    public static Set<TestEntity> getTestsBySessionId(String sessionId) {
        return get().tests.get(sessionId);
    }

    public static TestEntity getTestByName(String sessionId, String testName) {
        return getTestsBySessionId(sessionId).stream().filter((t)->t.getName().equals(testName)).findFirst().orElse(null);
    }

    public static void setTests(Map<String, Set<TestEntity>> tests) {
        get().tests = tests;
    }

    public static void addTests(String sessionId, Set<TestEntity> sessionTests) {
        get().tests.put(sessionId, sessionTests);
    }

    /**
     * Returns set of expected DB configs (for testing of GET /jaas/dbs).
     */
    public static Set<DbConfigEntity> getDbConfigs() {
        return get().dbConfigs;
    }

    /**
     * Returns NULL if no matching DB config entity found.
     *
     */
    public static DbConfigEntity getDbConfig(Long id) {
        return get().dbConfigs.stream().filter((c) -> id.equals(c.getId())).findFirst().orElse(null);
    }

    public static void setDbConfigs(Set<DbConfigEntity> dbConfigs) {
        get().dbConfigs = dbConfigs;
    }

    public static Set<DbConfigEntity> getDbConfigsToCreate() {
        return get().dbConfigsToCreate;
    }

    public static void setDbConfigsToCreate(Set<DbConfigEntity> dbConfigsToCreate) {
        get().dbConfigsToCreate = dbConfigsToCreate;
    }

    public static void addDbConfigToCreate(DbConfigEntity dbConfigToCreate) {
        get().dbConfigsToCreate.add(dbConfigToCreate);
    }

    public static List<String> getCreatedDbConfigIds() {
        return get().createdDbConfigIds;
    }

    public static void setCreatedDbConfigIds(List<String> createdDbConfigIds) {
        get().createdDbConfigIds = createdDbConfigIds;
    }

    public static void addCreatedDbConfigId(String createdDbConfigId) {
        get().createdDbConfigIds.add(createdDbConfigId);
    }
}