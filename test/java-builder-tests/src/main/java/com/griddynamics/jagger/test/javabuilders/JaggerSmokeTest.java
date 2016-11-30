package com.griddynamics.jagger.test.javabuilders;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileRps;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.RequestsPerSecond;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JaggerSmokeTest {
    public JLoadScenario jTestSuites() {
        JTestDefinition sleep_service_5ms_simple_http_query = JTestDefinition
                .builder(Id.of("sleep-service-5ms-simple-http-query-provider"), getEndpoints())
                .withComment("sleep-service-5ms-simple-http-query-provider")
                .withQueryProvider(getQueries())
                .withValidators(Collections.singletonList(NotNullResponseValidator.class))
//   TODO add the following things after clarifying
//  .addListeners(new NotNullInvocationListener()) TODO JFG-979
//  .addMetrics("metric-success-rate", "metric-not-null-response")
//  .withQueryDistributor("query-distributor-round-robin") TODO JFG-962
                .build();


        JLoadProfile load = JLoadProfileRps.builder(RequestsPerSecond.of(100)).withMaxLoadThreads(1).withWarmUpTimeInSeconds(1).build();

        JTerminationCriteria termination = new JTerminationCriteriaIterations(IterationsNumber.of(1000), MaxDurationInSeconds.of(500));

        JLoadTest test1 = JLoadTest.builder(Id.of("sleep-service-5ms-simple-http-query-provider"), sleep_service_5ms_simple_http_query, load, termination).build();

        JParallelTestsGroup testGroup = JParallelTestsGroup.builder(Id.of("sleep-service-5ms-http-query-provider"), test1).build();

        return JLoadScenario.builder(Id.of("ciJaggerTestSuite"), testGroup).build();
    }

    private Iterable<JHttpEndpoint> getEndpoints() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Collections.singletonList(new JHttpEndpoint("http://localhost:8080"));
    }

    private Iterable<JHttpQuery> getQueries() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Stream.of("55", "12", "77").map(q -> new JHttpQuery().get().path("/sleep", q)).collect(Collectors.toList());
    }

}