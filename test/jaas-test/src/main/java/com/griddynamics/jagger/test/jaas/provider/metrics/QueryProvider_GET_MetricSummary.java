package com.griddynamics.jagger.test.jaas.provider.metrics;

import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.Map;

/**
 * Provides a query for /jaas/sessions/{sessionId}/tests/{tesName}/metrics/summary resource which shall return list of available summaries.
 */
public class QueryProvider_GET_MetricSummary extends QueryProvider_GET_TestMetrics {

    @Value("${jaas.rest.sub.tests.metrics_summary}")
    private String summarySubPath;

    public QueryProvider_GET_MetricSummary() {
    }

    @Override
    public Iterator iterator() {
        if (queries.isEmpty()) {
            queries.add(new JHttpQuery<String>()
                    .get().responseBodyType(Map.class)
                    .path(getSummaryPath()));
        }

        return queries.iterator();
    }

    private String getSummaryPath() {
        return getMetricsPath() + summarySubPath;
    }
}