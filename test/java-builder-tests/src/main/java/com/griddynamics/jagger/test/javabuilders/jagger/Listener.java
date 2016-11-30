package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.AvgMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.MetricDescription;
import com.griddynamics.jagger.engine.e1.collector.StdDevMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationInfo;
import com.griddynamics.jagger.engine.e1.collector.invocation.InvocationListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.invoker.v2.JHttpResponse;


public class Listener extends ServicesAware implements Provider<InvocationListener<JHttpQuery, JHttpResponse, JHttpEndpoint>> {

    private static final String metric_name = "sin-metric";

    @Override
    protected void init() {
        getMetricService().createMetric(new MetricDescription(metric_name)
                .displayName("Sin metric")
                .showSummary(true).plotData(true)
                .addAggregator(new AvgMetricAggregatorProvider())
                .addAggregator(new StdDevMetricAggregatorProvider()));
    }

    @Override
    public InvocationListener<JHttpQuery, JHttpResponse, JHttpEndpoint> provide() {
        return new InvocationListener<JHttpQuery, JHttpResponse, JHttpEndpoint>() {
            private double step = 0;

            @Override
            public void onStart(InvocationInfo<JHttpQuery, JHttpResponse, JHttpEndpoint> invocationInfo) {
                double result = Math.sin(step);
                step += Math.PI/invocationInfo.getDuration();
                getMetricService().saveValue(metric_name, result);
            }
        };
    }
}
