package com.griddynamics.jagger.test.javabuilders.config;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.test.javabuilders.JaasSmokeTest;
import com.griddynamics.jagger.test.javabuilders.JaggerSmokeTest;
import com.griddynamics.jagger.test.javabuilders.jagger.JaggerTestSuit;
import com.griddynamics.jagger.test.javabuilders.jagger.TrueValidator;
import com.griddynamics.jagger.user.test.configurations.JLoadScenario;
import com.griddynamics.jagger.user.test.configurations.JLoadTest;
import com.griddynamics.jagger.user.test.configurations.JParallelTestsGroup;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;


@Configuration
public class JLoadScenariosConfig {
    
    @Bean
    public JLoadScenario jaasSmokeTest() {
        return new JaasSmokeTest().getHttpScenario();
    }

    @Bean
    public JLoadScenario jaagerSmokeTest() {
        return new JaggerSmokeTest().getJaggerScenario();
    }

    @Bean
    public JLoadScenario jaggetTests(){
        return new JaggerTestSuit().getJaggerTests();
    }

}
