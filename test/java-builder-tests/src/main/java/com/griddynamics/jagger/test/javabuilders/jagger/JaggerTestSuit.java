package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.test.javabuilders.config.JaggerPropertiesProvider;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaBackground;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;

import java.util.Arrays;


public class JaggerTestSuit {

    private final TestDefinitionVariations definitions;
    private final TestLoadVariations loads;
    private final JTerminationCriteria terminate10Sec;

    public JaggerTestSuit(JaggerPropertiesProvider properties) {
        definitions = new TestDefinitionVariations(properties);
        loads = new TestLoadVariations();
        terminate10Sec = JTerminationCriteriaDuration.of(DurationInSeconds.of(10));
    }

    /**
     * Check jagger with
     *  - definition has all fields with default values
     *  - rps load with one request per second and all fields with default values
     *  - termination by duration after 10 sec
     */
    private JLoadTest allDefaultsRps(){
        return JLoadTest.builder(Id.of("all definition default, all rps default, termination by duration"),
                definitions.allDefaults(),
                loads.oneRPSWithAllDefaults(),
                terminate10Sec)
                .build();
    }

    /**
     * Check jagger with
     *  - definition where all fields are specified
     *  - rps load with 100 requests per sec with max thread = 10 and warm up = 2 sec (all fields are specified)
     *  - termination after 2 iteration
     */
    private JLoadTest allFieldsRps(){
        return JLoadTest.builder(Id.of("all definition fields, all rps fields, termination by iteration"),
                definitions.allFields(),
                loads.rpsAllFields(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(2), MaxDurationInSeconds.of(60)))
                .build();
    }

    /**
     * Check jagger with
     *  - several queries in definition
     *  - rps load with 10 requests and 5 sec warm up
     *  - and termination by limit of duration
     */
    private JLoadTest queriesRotationWithWarmUp(){
        return JLoadTest.builder(Id.of("Queries rotation with warm up and termination by max duration"),
                definitions.listOfQueries(),
                loads.rpsFiveSecWarmUp(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(500), MaxDurationInSeconds.of(10)))
                .build();
    }

    /**
     * Check jagger with
     *  - definition with one query
     *  - rps load with 1 requests and 1 thread max
     */
    private JLoadTest oneQueryOneThread(){
        return JLoadTest.builder(Id.of("1 query, 1 thread"),
                definitions.singleQuery(),
                loads.rpsOneThreadMax(),
                terminate10Sec)
                .build();
    }

    /**
     * Check jagger with
     *  - definition where only comment is specified (other fields are default)
     *  - rps load with 1 requests and 0 sec warm up
     */
    private JLoadTest zeroWarmUp(){
        return JLoadTest.builder(Id.of("zero warm up, definition with comment"),
                definitions.withComment(),
                loads.rpsWith0WarmUp(),
                terminate10Sec)
                .build();
    }

    /**
     * Check jagger with
     *  - definition with single validator
     *  - group load with one group with one user with all fields with default values
     *  - termination after one iteration
     */
    private JLoadTest groupLoadAllDefaultAndValidator(){
        return JLoadTest.builder(Id.of("Single group with one user and all defaults, single validator, one iteration"),
                definitions.singleValidator(),
                loads.singleGroupAllDefaults(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(1), MaxDurationInSeconds.of(30)))
                .build();
    }

    /**
     * Check jagger with
     *  - definition with a few validators
     *  - group load with several user groups with different set of parameters
     */
    JLoadTest listOfValidatorsAndGroups(){
        return JLoadTest.builder(Id.of("User groups load"),
                definitions.listOfValidators(),
                loads.severalGroupWithUsersVariations(),
                JTerminationCriteriaDuration.of(DurationInSeconds.of(30)))
                .build();
    }

    /**
     * test with cpu load to test background termination, will be used to test metrics
     */
    private JLoadTest cpuLoadWithBackgroundTermination(){
        return JLoadTest.builder(Id.of("cpu load"),
                TestDefinitions.load_cpu_service_10000000(),
                loads.oneRPSWithAllDefaults(),
                JTerminationCriteriaBackground.getInstance())
                .build();
    }

    /**
     * test with memory load, will be used to test metrics
     */
    private JLoadTest allocateMemoryDuring10Sec(){
        return JLoadTest.builder(Id.of("allocate memory"),
                TestDefinitions.allocate_memory_service_1000000x200(),
                loads.oneRPSWithAllDefaults(),
                terminate10Sec)
                .build();
    }


    /**
     * Check jagger scenario with few test groups
     */
    public JLoadScenario getJaggerTests(){
        JParallelTestsGroup singleTest = JParallelTestsGroup.builder(Id.of("single test"), allDefaultsRps()).build();
        JParallelTestsGroup backgroundTermination = JParallelTestsGroup.builder(Id.of("background termination"),
                cpuLoadWithBackgroundTermination(), allocateMemoryDuring10Sec()).build();
        JParallelTestsGroup severalTests = JParallelTestsGroup.builder(Id.of("several tests"), Arrays.asList(
                zeroWarmUp(),
                groupLoadAllDefaultAndValidator(),
                queriesRotationWithWarmUp(),
                oneQueryOneThread(),
                //listOfValidatorsAndGroups(), TODO uncomment when JFG-1029 will be fixed
                allFieldsRps()
                )
        ).build();

        return JLoadScenario.builder(Id.of("JaggerTests"), singleTest, backgroundTermination, severalTests).build();
    }


}
