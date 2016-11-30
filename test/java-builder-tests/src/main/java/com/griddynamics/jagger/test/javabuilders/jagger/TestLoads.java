package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileRps;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.RequestsPerSecond;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteria;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaDuration;
import com.griddynamics.jagger.user.test.configurations.termination.JTerminationCriteriaIterations;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.DurationInSeconds;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.IterationsNumber;
import com.griddynamics.jagger.user.test.configurations.termination.auxiliary.MaxDurationInSeconds;

public class TestLoads {

    public JLoadProfile loadThreads(int n){
        return JLoadProfileUserGroups.builder(JLoadProfileUsers.builder(NumberOfUsers.of(n)).build()).build();
    }

    public JLoadProfile loadInvocation(){
        /*
        <load xsi:type="load-invocation" exactcount="1" threads="1"/>
        <load xsi:type="load-invocation" exactcount="100" threads="5"/>
        <load xsi:type="load-invocation" exactcount="10" threads="1" period="5s" delay="1000"/>
         */
        return null;
    }

    public JLoadProfile loadRps() {
        return JLoadProfileRps.builder(RequestsPerSecond.of(500)).withWarmUpTimeInSeconds(10).withMaxLoadThreads(10).build();
    }

    public JLoadProfile loadTps(){
        //<load xsi:type="load-tps" warmUpTime="100s" value="600" maxThreadNumber="500"  tickInterval="500"/>
        return null;
    }

    public JTerminationCriteria oneMin(){
        return JTerminationCriteriaDuration.of(DurationInSeconds.of(60));
    }

    public JTerminationCriteria iterations(){
        return JTerminationCriteriaIterations.of(IterationsNumber.of(1000), MaxDurationInSeconds.of(2*60*60));
    }

}
