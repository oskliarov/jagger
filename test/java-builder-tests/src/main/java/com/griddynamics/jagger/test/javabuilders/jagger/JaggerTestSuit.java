package com.griddynamics.jagger.test.javabuilders.jagger;

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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JaggerTestSuit {

    private final TestDefinitionVariations definitions;
    private final TestLoadVariations loads;
    private final JTerminationCriteria terminate10Sec;

    public JaggerTestSuit() {
        definitions = new TestDefinitionVariations();
        loads = new TestLoadVariations();
        terminate10Sec = JTerminationCriteriaDuration.of(DurationInSeconds.of(10));
    }

    JLoadTest allDefaultsRps(){
        return JLoadTest.builder(Id.of("all definition default, all rps default, termination by duration"),
                definitions.allDefaults(),
                loads.oneRPSWithAllDefaults(),
                terminate10Sec)
                .build();
    }

    JLoadTest allFieldsRps(){
        return JLoadTest.builder(Id.of("all definition fields, all rps fields, termination by iteration"),
                definitions.allFields(),
                loads.rpsAllFields(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(1), MaxDurationInSeconds.of(60)))
                .build();
    }

    JLoadTest queriesRotationWithWarmUp(){
        return JLoadTest.builder(Id.of("Queries rotation with warm up and termination by max duration"),
                definitions.listOfQueries(),
                loads.rpsFiveSecWarmUp(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(500), MaxDurationInSeconds.of(10)))
                .build();
    }

    JLoadTest oneQueryOneThread(){
        return JLoadTest.builder(Id.of("1 query, 1 thread"),
                definitions.singleQuery(),
                loads.rpsOneThreadMax(),
                terminate10Sec)
                .build();
    }

    JLoadTest zeroWarmUp(){
        return JLoadTest.builder(Id.of("zero warm up, definition with comment"),
                definitions.withComment(),
                loads.rpsWith0WarmUp(),
                terminate10Sec)
                .build();
    }

    JLoadTest groupLoadAllDefaultAndValidator(){
        return JLoadTest.builder(Id.of("Single group with one user and all defaults, single validator, one iteration"),
                definitions.singleValidator(),
                loads.singleGroupAllDefaults(),
                JTerminationCriteriaIterations.of(IterationsNumber.of(1), MaxDurationInSeconds.of(30)))
                .build();
    }

    JLoadTest listOfValidatorsAndGroups(){
        return JLoadTest.builder(Id.of("User groups load"),
                definitions.listOfValidators(),
                loads.severalGroupWithUsersVariations(),
                JTerminationCriteriaDuration.of(DurationInSeconds.of(30)))
                .build();
    }

    JLoadTest cpuLoadWithBackgroundTermination(){
        return JLoadTest.builder(Id.of("cpu load"),
                TestDefinitions.load_cpu_service_10000000(),
                loads.oneRPSWithAllDefaults(),
                JTerminationCriteriaBackground.getInstance())
                .build();
    }

    JLoadTest allocateMemoryDuring10Sec(){
        return JLoadTest.builder(Id.of("allocate memory"),
                TestDefinitions.allocate_memory_service_1000000x200(),
                loads.oneRPSWithAllDefaults(),
                terminate10Sec)
                .build();
    }


    public JLoadScenario getJaggerTests(){
        JParallelTestsGroup singleTest = JParallelTestsGroup.builder(Id.of("single test"), allDefaultsRps()).build();
        JParallelTestsGroup backgroundTermination = JParallelTestsGroup.builder(Id.of("background termination"),
                cpuLoadWithBackgroundTermination(), allocateMemoryDuring10Sec()).build();
        List<JParallelTestsGroup> groups = Stream.of(listOfValidatorsAndGroups(),
                groupLoadAllDefaultAndValidator(), zeroWarmUp(), queriesRotationWithWarmUp(), oneQueryOneThread(),
                allFieldsRps())
                .map(t -> JParallelTestsGroup.builder(Id.of(t.getId()), t).build())
                .collect(Collectors.toList());
        groups.add(backgroundTermination);

        return JLoadScenario.builder(Id.of("JaggerTests"), singleTest, groups.toArray(new JParallelTestsGroup[]{})).build();
    }

}
