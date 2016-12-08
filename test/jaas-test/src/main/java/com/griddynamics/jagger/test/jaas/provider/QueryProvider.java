package com.griddynamics.jagger.test.jaas.provider;

import com.griddynamics.jagger.engine.e1.services.data.service.MetricEntity;
import com.griddynamics.jagger.engine.e1.services.data.service.SessionEntity;
import com.griddynamics.jagger.engine.e1.services.data.service.TestEntity;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;


public class QueryProvider {
    private String HDR_CONTENT_TYPE = "Content-Type";
    private String HDR_CONTENT_TYPE_VALUE_APP_JSON = "application/json";

    private final String sessions_uri;
    private final String tests_uri;
    private final String db_uri;
    private Function<String, String> getPropertyValue;

    public QueryProvider(Function<String, String> getPropertyValue) {
        this.getPropertyValue = getPropertyValue;
        sessions_uri = getValue("jaas.rest.base.sessions");
        tests_uri = getValue("jaas.rest.sub.sessions.tests");
        db_uri = getValue("jaas.rest.base.dbs");
    }

    private String getValue(String key){
        return getPropertyValue.apply(key);
    }

    public Iterable GET_SessionsList() {
        return () -> Collections.singletonList(new JHttpQuery<String>().get().responseBodyType(SessionEntity[].class).path(sessions_uri)).iterator();
    }

    public Iterable GET_SessionIds() {
        return () -> TestContext
                .getSessions()
                .stream()
                .map(s -> new JHttpQuery<String>()
                        .get().responseBodyType(SessionEntity.class).path(sessions_uri + "/" + s.getId()))
                .iterator();
    }

    public Iterable GET_TestsList() {
        return () -> Collections.singletonList(new JHttpQuery<String>()
                .get().responseBodyType(TestEntity[].class)
                .path(sessions_uri + "/" + getSessionId() + tests_uri))
                .iterator();
    }

    public Iterable GET_TestNames() {
        return ()->TestContext.getTestsBySessionId(getSessionId())
                .stream().map(t -> new JHttpQuery<String>()
                        .get().responseBodyType(TestEntity.class)
                        .path(sessions_uri + "/" + getSessionId() + tests_uri + "/" + t.getName()))
                .iterator();
    }

    public Iterable GET_TestMetrics() {
        return () -> Collections.singletonList(new JHttpQuery<String>()
                .get().responseBodyType(MetricEntity[].class)
                .path(getMetricPath())).iterator();
    }

    public Iterable GET_MetricSummary() {
        return () -> Collections.singletonList(new JHttpQuery<String>()
                .get().responseBodyType(Map.class)
                .path(getMetricPath() + getValue("jaas.rest.sub.tests.metrics_summary")))
                .iterator();
    }

    public Iterable GET_MetricPlotData() {
        return ()->Collections.singletonList(new JHttpQuery<String>()
                .get().responseBodyType(Map.class)
                .path(getMetricPath() + getValue("jaas.rest.sub.tests.metrics_plot_data")))
                .iterator();
    }

    public Iterable GET_DBsList() {
        return ()->Collections.singletonList(new JHttpQuery<String>()
                .get().responseBodyType(DbConfigEntity[].class).path(db_uri))
                .iterator();
    }

    public Iterable GET_DBIds() {
        return ()->TestContext
                .getDbConfigs()
                .stream()
                .map(c -> new JHttpQuery<String>()
                        .get().responseBodyType(DbConfigEntity.class).path(db_uri + "/" + c.getId()))
                .iterator();
    }

    public Iterable GET_NonNumeric_DBIds() {
        return ()->Stream.of("/abvgdeyka", "/ABVGD")
                .map(q -> new JHttpQuery<String>()
                        .get().responseBodyType(String.class).path(db_uri + q))
                .iterator();
    }

    public Iterable GET_NonExisting_DBIds() {
        return ()->Stream.of(Integer.MAX_VALUE, Integer.MIN_VALUE)
                .map(q -> new JHttpQuery<String>()
                        .get().responseBodyType(String.class).path(db_uri + "/" + q))
                .iterator();
    }

    public Iterable GET_CreatedDBIds() {
        return ()->TestContext
                .getCreatedDbConfigIds()
                .stream()
                .map(id -> new JHttpQuery<String>()
                        .get().responseBodyType(DbConfigEntity.class)
                        .path(db_uri + "/" + id))
                .iterator();
    }

    public Iterable PUT_DB() {
        return () -> {
            List<JHttpQuery<String>> queries = new LinkedList<>();

            for (String id : TestContext.getCreatedDbConfigIds()) {
                DbConfigEntity conf = TestContext.provideFakeDbConfig_NoId();
                conf.setId(new Long(id));
                conf.setDesc("MODIFIED " + conf.getDesc());
                TestContext.addDbConfig(conf); //Assumption - POST test(s) passed and there are newly created records.

                queries.add((new JHttpQuery<String>()
                        .put()
                        .body(conf.toJson())
                        .header(HDR_CONTENT_TYPE, HDR_CONTENT_TYPE_VALUE_APP_JSON)
                        .path(db_uri + "/" + id)));
            }
            return queries.iterator();
        };
    }

    public Iterable POST_DB() {
        return ()->Collections.singletonList(new JHttpQuery<String>()
                .post()
                .body(TestContext.getDbConfigPrototype().toJson())
                .header(HDR_CONTENT_TYPE, HDR_CONTENT_TYPE_VALUE_APP_JSON)
                .path(db_uri))
                .iterator();
    }

    public Iterable DELETE_DB() {
        return ()->Collections.singletonList(new JHttpQuery<String>()
                .delete().path(db_uri + "/" + TestContext.getCreatedDbConfigIds().get(0)))
                .iterator();
    }

    public Iterable GET_Deleted_DB() {
        return ()->Collections.singletonList(new JHttpQuery<String>()
                .get().path(db_uri + "/" + TestContext.getCreatedDbConfigIds().get(0)))
                .iterator();
    }

    private String getMetricPath() {
        return sessions_uri + "/" + getSessionId() + tests_uri + "/"
                + TestContext.getMetrics().get(getSessionId()).keySet().toArray(new String[]{})[0]
                + getValue("jaas.rest.sub.tests.metrics");
    }

    private String getSessionId() {
        return (TestContext.getTests().keySet().toArray(new String[]{}))[0];
    }

}
