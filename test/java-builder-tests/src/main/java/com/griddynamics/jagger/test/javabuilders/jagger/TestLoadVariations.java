package com.griddynamics.jagger.test.javabuilders.jagger;


import com.griddynamics.jagger.user.test.configurations.load.JLoadProfile;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileRps;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUserGroups;
import com.griddynamics.jagger.user.test.configurations.load.JLoadProfileUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.NumberOfUsers;
import com.griddynamics.jagger.user.test.configurations.load.auxiliary.RequestsPerSecond;

class TestLoadVariations {
    JLoadProfile oneRPSWithAllDefaults(){
        return JLoadProfileRps.builder(RequestsPerSecond.of(1)).build();
    }

    JLoadProfile rpsFiveSecWarmUp(){
        return JLoadProfileRps.builder(RequestsPerSecond.of(1))
                .withWarmUpTimeInSeconds(5)
                .build();
    }

    JLoadProfile rpsWith0WarmUp(){
        return JLoadProfileRps.builder(RequestsPerSecond.of(1))
                .withWarmUpTimeInSeconds(0)
                .build();
    }

    JLoadProfile rpsOneThreadMax(){
        return JLoadProfileRps.builder(RequestsPerSecond.of(1))
                .withMaxLoadThreads(1)
                .build();
    }

    JLoadProfile rpsAllFields(){
        return JLoadProfileRps.builder(RequestsPerSecond.of(100))
                .withMaxLoadThreads(10)
                .withWarmUpTimeInSeconds(2)
                .build();
    }

    JLoadProfile singleGroupAllDefaults(){
        JLoadProfileUsers group = JLoadProfileUsers.builder(NumberOfUsers.of(1)).build();
        return JLoadProfileUserGroups.builder(group).withDelayBetweenInvocationsInSeconds(1)
                .build();
    }

    JLoadProfile severalGroupWithUsersVariations(){
        JLoadProfileUsers withLifeTime = JLoadProfileUsers.builder(NumberOfUsers.of(10))
                .withLifeTimeInSeconds(2)
                .build();
        JLoadProfileUsers withStartDelay = JLoadProfileUsers.builder(NumberOfUsers.of(50))
                .withStartDelayInSeconds(1)
                .build();
        JLoadProfileUsers withSlewRate = JLoadProfileUsers.builder(NumberOfUsers.of(20))
                .withSlewRateUsersPerSecond(5)
                .build();
        JLoadProfileUsers withAllFields = JLoadProfileUsers.builder(NumberOfUsers.of(40))
                .withLifeTimeInSeconds(5)
                .withSlewRateUsersPerSecond(5)
                .withStartDelayInSeconds(2)
                .build();

        return JLoadProfileUserGroups.builder(withLifeTime, withStartDelay, withSlewRate, withAllFields)
                .build();
    }



}
