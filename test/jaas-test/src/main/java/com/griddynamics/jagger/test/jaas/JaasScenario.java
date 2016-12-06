package com.griddynamics.jagger.test.jaas;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.test.jaas.provider.QueryProvider;
import com.griddynamics.jagger.test.jaas.validator.*;
import com.griddynamics.jagger.test.jaas.validator.dbs.CreateDBResponseValidator;
import com.griddynamics.jagger.test.jaas.validator.dbs.CreatedDBContentValidator;
import com.griddynamics.jagger.test.jaas.validator.dbs.DBResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.dbs.DBsListResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.metrics.MetricsListResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.metrics.PlotListResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.metrics.SummaryListResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.sessions.SessionResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.sessions.SessionsListResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.tests.TestResponseContentValidator;
import com.griddynamics.jagger.test.jaas.validator.tests.TestsListResponseContentValidator;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.limits.JLimitVsRefValue;
import com.griddynamics.jagger.user.test.configurations.limits.auxiliary.RefValue;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;
import com.griddynamics.jagger.util.JaggerPropertiesProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
public class JaasScenario extends JaggerPropertiesProvider {

    @Bean
    public JLoadScenario getJaasTestScenario() {
        QueryProvider queryProvider = new QueryProvider(this::getPropertyValue);

        JParallelTestsGroup tg_JaaS_GET_Sessions = JParallelTestsGroup.builder(Id.of("tg_JaaS_GET_Sessions"),
                getStandardTest("t_JaaS_GET_Sessions_List_SR_eq._1.0", queryProvider.GET_SessionsList(), SessionsListResponseContentValidator.class),
                getStandardTest("t_JaaS_GET_Exact_Session_SR_eq._1.0", queryProvider.GET_SessionIds(), SessionResponseContentValidator.class)
        ).build();

        JParallelTestsGroup tg_JaaS_GET_Tests = JParallelTestsGroup.builder(Id.of("tg_JaaS_GET_Tests"),
                getStandardTest("t_JaaS_GET_Session_Tests_List_SR_eq._1.0", queryProvider.GET_TestsList(), TestsListResponseContentValidator.class),
                getStandardTest("t_JaaS_GET_Exact_Session_Test_SR_eq._1.0", queryProvider.GET_TestNames(), TestResponseContentValidator.class)
        ).build();

        JParallelTestsGroup tg_JaaS_GET_Metrics = getSingleStandardTest("t_JaaS_GET_Metrics_List_SR_eq._1.0",
                queryProvider.GET_TestMetrics(), MetricsListResponseContentValidator.class);

        JParallelTestsGroup tg_JaaS_GET_Metric_Summaries = getSingleStandardTest("t_JaaS_GET_Metric_Summary_List_SR_eq",
                queryProvider.GET_MetricSummary(), SummaryListResponseContentValidator.class);

        JParallelTestsGroup td_JaaS_MetricPlotList = getSingleStandardTest("t_JaaS_GET_Metric_Plot_List_SR_eq._1.0",
                queryProvider.GET_MetricPlotData(), PlotListResponseContentValidator.class);

        JParallelTestsGroup tg_JaaS_GET_DB_configs = JParallelTestsGroup.builder(Id.of("tg_JaaS_GET_DB_configs"),
                getStandardTest("t_JaaS_GET_DBs_List_SR_eq._1.0", queryProvider.GET_DBsList(), DBsListResponseContentValidator.class),
                getStandardTest("t_JaaS_GET_Exact_DB_SR_eq._1.0", queryProvider.GET_DBIds(), DBResponseContentValidator.class),
                getStandardTest("t_JaaS_GET_DBsIds_Non_Numeric", queryProvider.GET_NonNumeric_DBIds(),
                        Arrays.asList(NotNullResponseValidator.class, ResponseStatus400Validator.class, BadRequest_ResponseContentValidator.class)),
                getStandardTest("t_JaaS_GET_DBsIds_Not_Existent", queryProvider.GET_NonExisting_DBIds(),
                        Arrays.asList(NotNullResponseValidator.class, ResponseStatus404Validator.class))
        ).build();

        JParallelTestsGroup tg_JaaS_GET_After_POST_DB_configs = getSingleStandardTest("t_JaaS_GET_Created_DB_SR_eq._1.0",
                queryProvider.GET_CreatedDBIds(), CreatedDBContentValidator.class);

        JParallelTestsGroup tg_JaaS_PUT_After_POST_DB_configs = JParallelTestsGroup.builder(Id.of("tg_JaaS_PUT_After_POST_DB_configs"),
                getStandardTest("t_JaaS_PUT_Created_DB_SR_eq._1.0", queryProvider.PUT_DB(),
                        Arrays.asList(NotNullResponseValidator.class, ResponseStatus202Validator.class))
        ).build();

        JParallelTestsGroup tg_JaaS_POST_DB_configs = JParallelTestsGroup.builder(Id.of("tg_JaaS_POST_DB_configs"),
                getInvocationTest("t_JaaS_POST_DB_SR_eq._1.0", queryProvider.POST_DB(),
                        Arrays.asList(CreateDBResponseValidator.class, ResponseStatus201Validator.class, NotNullResponseValidator.class))
        ).build();

        JParallelTestsGroup tg_JaaS_DELETE_DB_configs = JParallelTestsGroup.builder(Id.of("tg_JaaS_DELETE_DB_configs"),
                getInvocationTest("t_JaaS_DELETE_DB_SR_eq._1.0", queryProvider.DELETE_DB(),
                        Arrays.asList(ResponseStatus204Validator.class, NotNullResponseValidator.class))
        ).build();

        JParallelTestsGroup tg_JaaS_GET_After_DELETE_DB_configs = JParallelTestsGroup.builder(Id.of("tg_JaaS_GET_After_DELETE_DB_configs"),
                getInvocationTest("t_JaaS_GET_Deleted_DB", queryProvider.GET_Deleted_DB(),
                        Arrays.asList(NotNullResponseValidator.class, ResponseStatus404Validator.class))
        ).build();

        return JLoadScenario.builder(Id.of("ts_JaaSTestSuit"), tg_JaaS_GET_Sessions,
                tg_JaaS_GET_Tests, tg_JaaS_GET_Metrics, tg_JaaS_GET_Metric_Summaries, td_JaaS_MetricPlotList,
                tg_JaaS_GET_DB_configs, tg_JaaS_POST_DB_configs,
                tg_JaaS_GET_After_POST_DB_configs, tg_JaaS_PUT_After_POST_DB_configs,
                tg_JaaS_DELETE_DB_configs, tg_JaaS_GET_After_DELETE_DB_configs)
                .withLatencyPercentiles(Collections.singletonList(99d))
                // .withListener(TestSuiteConfigListener)
                .build();
    }


    private JParallelTestsGroup getSingleStandardTest(String id, Iterable queryProvider, Class<? extends ResponseValidator> validator) {
        return JParallelTestsGroup.builder(Id.of("g" + id), getStandardTest(id, queryProvider, validator)).build();
    }

    private JLoadTest getStandardTest(String id, Iterable queryProvider, Class<? extends ResponseValidator> validator) {
        return getStandardTest(id, queryProvider,
                Arrays.asList(NotNullResponseValidator.class, ResponseStatusValidator.class, validator));
    }

    private JLoadTest getStandardTest(String id, Iterable queryProvider, List<Class<? extends ResponseValidator>> validators) {
        JLoadProfile standardGroupLoad = JLoadProfileUserGroups
                .builder(JLoadProfileUsers.builder(NumberOfUsers.of(2))
                        .withLifeTimeInSeconds(Long.valueOf(getPropertyValue("jaas.std.tst.life")))
                        .build())
                .build();
        JTerminationCriteria standardTermination = JTerminationCriteriaIterations
                .of(IterationsNumber.of(Long.valueOf(getPropertyValue("jaas.std.tst.iterations"))),
                    MaxDurationInSeconds.of(Long.valueOf(getPropertyValue("jaas.std.tst.max_duration"))));

        return getCommonLoadTest(id, queryProvider, validators, standardGroupLoad, standardTermination);
    }

    private JLoadTest getInvocationTest(String id, Iterable queryProvider, List<Class<? extends ResponseValidator>> validators) {
        JLoadProfile oneInvocation = JLoadProfileUserGroups
                .builder(JLoadProfileUsers.builder(NumberOfUsers.of(1)).build())
                .build();
        JTerminationCriteria invocationTermination = JTerminationCriteriaIterations
                .of(IterationsNumber.of(1), MaxDurationInSeconds.of(1));

        return getCommonLoadTest(id, queryProvider, validators, oneInvocation, invocationTermination);
    }

    private JLoadTest getCommonLoadTest(String id, Iterable queryProvider, List<Class<? extends ResponseValidator>> validators, JLoadProfile standardGroupLoad, JTerminationCriteria standardTermination) {
        JTestDefinition definition = JTestDefinition.builder(Id.of(id + "def"),
                Collections.singletonList(new JHttpEndpoint(getPropertyValue("jaas.endpoint"))))
                .withQueryProvider(queryProvider)
                .withValidators(validators)
                .build();

        return JLoadTest.builder(Id.of(id), definition, standardGroupLoad, standardTermination)
                .withLimits(JLimitVsRefValue.builder("successRate", RefValue.of(1.0)).build())
                .build();
    }

}
