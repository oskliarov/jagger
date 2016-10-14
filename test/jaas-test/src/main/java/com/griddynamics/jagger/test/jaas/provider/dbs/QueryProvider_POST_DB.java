package com.griddynamics.jagger.test.jaas.provider.dbs;

import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.test.jaas.util.TestContext;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

/**
 * Provides queries like POST /jaas/dbs/{DB config data}.
 */
public class QueryProvider_POST_DB extends QueryProvider_DBsList {

    public QueryProvider_POST_DB() {
        generateDbConfigPrototype();
    }

    @Override
    public Iterator iterator() {
        if (queries.isEmpty()) {
            queries.add((new JHttpQuery<String>()
                        .post()
                        .body(TestContext.getDbConfigPrototype().toJson())
                        .header("Content-Type", Collections.singletonList("application/json")) //TODO: re-factor once JFG-934 is ready.
                        .path(uri)));
        }

        return queries.iterator();
    }

    /**
     */
    private void generateDbConfigPrototype() {
        DbConfigEntity dbConf = new DbConfigEntity();
        dbConf.setUser("Proto-" + UUID.randomUUID().toString());
        dbConf.setPass("[DELETE THIS]" + UUID.randomUUID().toString());
        dbConf.setDesc("Timestamp: " + LocalDateTime.now().toString());
        dbConf.setHibernateDialect(UUID.randomUUID().toString());
        dbConf.setJdbcDriver(UUID.randomUUID().toString());
        dbConf.setUrl("jdbc:"+UUID.randomUUID().toString());

        TestContext.setDbConfigToCreate(dbConf);
    }
}