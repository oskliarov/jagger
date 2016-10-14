package com.griddynamics.jagger.test.jaas.listener;

import com.alibaba.fastjson.JSON;
import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.testsuite.TestSuiteInfo;
import com.griddynamics.jagger.engine.e1.collector.testsuite.TestSuiteListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;
import com.griddynamics.jagger.engine.e1.services.data.service.SessionEntity;
import com.griddynamics.jagger.engine.e1.services.data.service.TestEntity;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Loads expected data into temp storage({@link TestContext}).
 * <p>
 * Created by ELozovan on 2016-09-27.
 */
public class TestSuiteConfigListener extends ServicesAware implements Provider<TestSuiteListener> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuiteConfigListener.class);

    @Value("${jaas.rest.dbs.expcted_dbs_path}")
    private String pathToExpectedDbConfigJSON;

    @Override
    public TestSuiteListener provide() {
        return new TestSuiteListener() {
            @Override
            public void onStart(TestSuiteInfo testSuiteInfo) {
                super.onStart(testSuiteInfo);
                Set<SessionEntity> sessionsAvailable = getDataService().getSessions(Collections.emptyList());
                sessionsAvailable.stream().forEach(this::correctDateFieldValue);
                TestContext.setSessions(sessionsAvailable);

                findAndLoadExpectedTests(sessionsAvailable);

                loadExpectedDbConfigs();
            }

            private void findAndLoadExpectedTests(Set<SessionEntity> sessionsAvailable) {
                SessionEntity sessionToGetTests = null;
                Set<TestEntity> tests = null;
                while (null == tests) {
                    sessionToGetTests = sessionsAvailable.stream().skip(new Random().nextInt(sessionsAvailable.size() - 1)).findFirst().orElse(null);
                    tests = getDataService().getTests(sessionToGetTests);

                    if (tests.isEmpty()) {
                        tests = null; //Let's find another session which shall have some tests stored.
                    }
                }

                tests.stream().forEach(this::correctDateFieldValue);
                TestContext.addTests(sessionToGetTests.getId(), tests);
            }

            private void loadExpectedDbConfigs() {
                try {
                    String tmp = new String(Files.readAllBytes(Paths.get(pathToExpectedDbConfigJSON).toAbsolutePath()));
                    List<DbConfigEntity> configs = JSON.parseArray(tmp, DbConfigEntity.class);
                    TestContext.setDbConfigs(configs.stream().collect(Collectors.toSet()));
                } catch (IOException e) {
                    LOGGER.warn("Could not read expected DB configs due to {}", e.getMessage(), e);
                }
            }

            /**
             * DataService returns dates as Timestamp, JSON deserialiser returns them as Date, so #equals() returns false anyway.
             * This crutch resets date fields values to avoid that type mismatch.
             */
            private <T> void correctDateFieldValue(T entity) {
                final String getterPrefix = "get";
                Method[] allMethods = entity.getClass().getDeclaredMethods();
                for (Method m : allMethods) {
                    String mName = m.getName();
                    Type mReturnType = m.getGenericReturnType();

                    // Looking for a Date getXYZ()
                    if (!(mName.startsWith(getterPrefix) && (mReturnType.equals(Date.class)))) {
                        continue;
                    }

                    m.setAccessible(true);
                    try {
                        Date adjustedValue = new Date(((Date) m.invoke(entity)).getTime());
                        Method theSetter = entity.getClass().getDeclaredMethod(mName.replace(getterPrefix, "set"), Date.class);
                        theSetter.setAccessible(true);
                        theSetter.invoke(entity, adjustedValue);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.warn("Was not able to correct date value({}) in the entity {}.", mName, entity);
                    }
                }
            }
        };
    }
}