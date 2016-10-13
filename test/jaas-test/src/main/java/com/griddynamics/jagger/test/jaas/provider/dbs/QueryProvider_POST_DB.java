package com.griddynamics.jagger.test.jaas.provider.dbs;

import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.test.jaas.util.TestContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Provides queries like POST /jaas/dbs/{DB config data}.
 */
public class QueryProvider_POST_DB extends QueryProvider_DBsList {

    public QueryProvider_POST_DB() {}

    @Override
    public Iterator iterator() {
        if (queries.isEmpty()) {
            queries.addAll(TestContext
                            .getDbConfigsToCreate()
                            .stream()
                            .map(c -> new JHttpQuery<String>()
                                            .post()
                                            .body(c.toJson())
                                            .header("Content-Type", Arrays.asList("application/json")) //TODO: re-factor as JFG-934 is ready.
                                            .path(uri))
                            .collect(Collectors.toList()));
        }

        return queries.iterator();
    }
}