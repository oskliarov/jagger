package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDescriptionVariations {
    private Iterable<JHttpEndpoint> getEndpoints() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Collections.singletonList(new JHttpEndpoint("http://localhost:8080"));
    }

    private Iterable<JHttpQuery> getQueries() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Stream.of("55", "12", "77").map(q -> new JHttpQuery().get().path("/sleep", q)).collect(Collectors.toList());
    }

    public JTestDefinition get(){
        return JTestDefinition.builder(Id.of("id"), getEndpoints()).withQueryProvider(getQueries()).build();
    }
}
