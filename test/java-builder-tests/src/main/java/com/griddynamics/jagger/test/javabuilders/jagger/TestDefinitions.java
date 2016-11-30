package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDefinitions {

    private final List<Class<? extends ResponseValidator>> validators = Arrays.asList(NotNullResponseValidator.class, TrueValidator.class);

    private static Iterable<JHttpEndpoint> getEndpoints() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Collections.singletonList(new JHttpEndpoint("http://localhost:8080"));
    }

    private Iterable<JHttpQuery> getQueries() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Stream.of("55", "12", "77").map(q -> new JHttpQuery().get().path("/sleep", q)).collect(Collectors.toList());
    }

    private static Iterable<JHttpQuery> getQuery(String path){
        return Collections.singletonList(new JHttpQuery().get().path(path));
    }

    public JTestDefinition sleep_service_rotate_query(){
        return JTestDefinition.builder(Id.of("sleep-service-rotate_query"), getEndpoints())
                .withQueryProvider(getQueries())
                .withValidators(validators)
                // .withMetric("metric-success-rate" plotData="true", "metric-not-null-response" plotData="true"
                // .withListener("listener-invocation-not-null-response")
                .build();
    }

    public JTestDefinition sleep_service_5ms() {
        return JTestDefinition.builder(Id.of("sleep-service-5ms"), getEndpoints())
                .withQueryProvider(getQuery("/sleep/5"))
                .withValidators(validators)
                .build();
//        <info-collectors>
//            <!--Math functions with standard aggregators-->
//            <metric id="constant1" xsi:type="metric-custom" calculator="constant" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//                <metric-aggregator xsi:type="metric-aggregator-std"/>
//                <metric-aggregator xsi:type="metric-aggregator-sum"/>
//            </metric>
//
//            <metric id="increase1" xsi:type="metric-custom" calculator="alwaysIncrease" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//                <metric-aggregator xsi:type="metric-aggregator-std"/>
//                <metric-aggregator xsi:type="metric-aggregator-sum"/>
//            </metric>
//
//            <metric id="sin1" xsi:type="metric-custom" calculator="sin" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//                <metric-aggregator xsi:type="metric-aggregator-std"/>
//                <metric-aggregator xsi:type="metric-aggregator-sum"/>
//            </metric>
//
//            <!-- begin: following section is used for docu generation - metrics with aggregators -->
//            <!-- Note: to use this example, you shall implement custom metric calculators "constant", "alwaysIncrease" and "sin". -->
//
//            <!-- Math functions with aggregators with settings -->
//            <!-- Normalize metric value by seconds -->
//            <metric id="constant1-sum-normalized-1s" xsi:type="metric-custom" calculator="constant" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-sum" normalizeBy="SECOND"/>
//            </metric>
//
//            <!-- Set number of points in the plots for this metric -->
//            <metric id="increase1-sum-point-count-10" xsi:type="metric-custom" calculator="alwaysIncrease" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-sum" pointCount="10"/>
//            </metric>
//
//            <!-- Set exact interval between points in the plots for this metric -->
//            <metric id="sin1-sum-point-interval-2s" xsi:type="metric-custom" calculator="sin" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-sum" pointInterval="2000"/>
//            </metric>
//
//            <!-- end: following section is used for docu generation - metrics with aggregators -->
//
//        </info-collectors>
    }

    public JTestDefinition sleep_service_50ms() {
        return JTestDefinition.builder(Id.of("sleep-service-50ms"), getEndpoints())
                .withQueryProvider(getQuery("/sleep/50"))
                .withValidators(validators)
                .build();
//        <info-collectors>
//
//            <!--Math functions with custom aggregators-->
//            <metric id="constant2" xsi:type="metric-custom" calculator="constant" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="maxAggregator"/>
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="minAggregator"/>
//            </metric>
//
//            <metric id="increase2" xsi:type="metric-custom" calculator="alwaysIncrease" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="maxAggregator"/>
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="minAggregator"/>
//            </metric>
//
//            <!--begin: following section is used for docu generation - custom aggregator usage-->
//            <metric id="sin2" xsi:type="metric-custom" calculator="sin" plotData="true" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="maxAggregator"/>
//                <metric-aggregator xsi:type="metric-aggregator-ref" ref="minAggregator"/>
//            </metric>
//            <!--end: following section is used for docu generation - custom aggregator usage-->
//
//        </info-collectors>
    }

    public JTestDefinition sleep_service_50_150ms(){
        return JTestDefinition.builder(Id.of("sleep-service-50-150ms"), getEndpoints())
                .withQueryProvider(getQuery("/sleep/50-150"))
                .withValidators(validators)
                // .withMetric("metric-success-rate" plotData="true", "metric-not-null-response" plotData="true"
                .build();
    }
//        <info-collectors>
//             <!--Success rate metrics-->
//            <!-- begin: following section is used for docu generation - metric-success-rate -->
//
//            <!--    Example #1: Success rate metric with default aggregators
//            plotData = true => metric vs time data will be saved (displayed in WebUI and PDF report)
//    saveSummary = true => summary value for summary page will be not saved -->
//            <metric xsi:type="metric-success-rate" plotData="true" saveSummary="false"/>
//
//
//
//            <!--    Example #2: Success rate metric with custom name and default aggregators -->
//            <metric id="CustomNameSuccessRate" xsi:type="metric-success-rate" plotData="true" saveSummary="false"/>
//
//
//
//            <!--    Example #3: Success rate metric with custom name and custom aggregators
//    With this setup success rate will be aggregated on interval like on picture below -->
//            <metric id="CustomSuccessRatePlot" xsi:type="metric-success-rate" plotData="true" saveSummary="false">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//            </metric>
//
//            <!-- end: following section is used for docu generation - metric-success-rate -->
//
//            <!-- begin: following section is used for docu generation - standard aggregator usage -->
//            <metric id="CustomSuccessRateSummary" xsi:type="metric-success-rate" saveSummary="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//            </metric>
//            <!-- end: following section is used for docu generation - standard aggregator usage -->


    public JTestDefinition pulse_service_30000_500_abstract(){
        return JTestDefinition.builder(Id.of("pulse-service-30000-500"), getEndpoints())
                .withQueryProvider(getQuery("/sleep/pulse/30000/500"))
                .withValidators(validators)
                // .withMetric("metric-success-rate" plotData="true", "metric-not-null-response" plotData="true"
                .build();
    }

//        <info-collectors>
//            <!--Response metrics-->
//            <metric id="BodySize" xsi:type="metric-custom" calculator="responseSize" plotData="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//                <metric-aggregator xsi:type="metric-aggregator-sum"/>
//            </metric>
//
//            <metric id="ReturnCode" xsi:type="metric-custom" calculator="returnCode" plotData="true">
//                <metric-aggregator xsi:type="metric-aggregator-avg"/>
//                <metric-aggregator xsi:type="metric-aggregator-sum"/>
//            </metric>
//        </info-collectors>

    static JTestDefinition load_cpu_service_10000000(){
        return JTestDefinition.builder(Id.of("load-cpu-service-10000000"), getEndpoints())
                .withQueryProvider(getQuery("/load/10000000"))
                .build();
    }

    static JTestDefinition allocate_memory_service_1000000x200(){
        return JTestDefinition.builder(Id.of("allocate-memory-service-1000000x200"), getEndpoints())
                .withQueryProvider(getQuery("/allocate/1000000x200"))
                .build();
    }

    public JTestDefinition tcp_service_10000(){
        return JTestDefinition.builder(Id.of("tcp-service-10000"), getEndpoints())
                .withQueryProvider(getQuery("/net/text/10000"))
                .build();
    }
}
