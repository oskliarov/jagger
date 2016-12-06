package com.griddynamics.jagger.test.jaas.provider.dbs;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.test.jaas.util.TestContext;

import java.util.Iterator;

/**
 * Provides queries like POST /jaas/dbs/{DB config data}.
 */
public class QueryProvider_POST_DB extends QueryProvider_GET_DBsList {

    public QueryProvider_POST_DB() {}

    @Override
    public Iterator iterator() {
        if (queries.isEmpty()) {
            queries.add((new JHttpQuery<String>()
                        .post()
                        .body(TestContext.getDbConfigPrototype().toJson())
                        .header(HDR_CONTENT_TYPE, HDR_CONTENT_TYPE_VALUE_APP_JSON)
                        .path(uri)));
        }

        return queries.iterator();
    }
}