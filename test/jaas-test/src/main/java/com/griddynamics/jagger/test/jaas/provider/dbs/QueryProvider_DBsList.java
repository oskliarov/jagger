package com.griddynamics.jagger.test.jaas.provider.dbs;

import com.griddynamics.jagger.invoker.http.v2.JHttpQuery;
import com.griddynamics.jagger.test.jaas.util.entity.DbConfigEntity;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides a query like GET /jaas/dbs which shall return list of known DB configs.
 */
public class QueryProvider_DBsList implements Iterable {
    protected List<JHttpQuery<String>> queries = new LinkedList<>();

    @Value( "${jaas.rest.base.dbs}" )
    protected String uri;

    public QueryProvider_DBsList() {}

    @Override
    public Iterator iterator() {
        if (queries.isEmpty()) {
            queries.add(new JHttpQuery<String>().get().responseBodyType(DbConfigEntity[].class).path(uri));
        }

        return queries.iterator();
    }
}