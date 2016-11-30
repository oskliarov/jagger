package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestDefinitionVariations {

    private static final List<JHttpQuery> SINGLE_QUERY = Collections.singletonList(new JHttpQuery().get().path("/sleep/10"));

    private Iterable<JHttpEndpoint> getEndpoints() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Collections.singletonList(new JHttpEndpoint("http://localhost:8080"));
    }

    private Iterable<JHttpQuery> getQueries() {
        // TODO oskliarov: when JFG-972 will be done use properties
        return Stream.of("55", "12", "77").map(q -> new JHttpQuery().get().path("/sleep", q)).collect(Collectors.toList());
    }

    JTestDefinition allDefaults(){
        return JTestDefinition.builder(Id.of("all defaults"), getEndpoints()).build();
    }

    JTestDefinition singleQuery(){
        return JTestDefinition.builder(Id.of("single query"), getEndpoints())
                .withQueryProvider(SINGLE_QUERY)
                .build();
    }

    JTestDefinition listOfQueries(){
        return JTestDefinition.builder(Id.of("queries list"), getEndpoints())
                .withQueryProvider(getQueries())
                .build();
    }

    JTestDefinition customInvoker(){
        // TODO create test with custom invoker when JFG-1018 will be fixed
        return JTestDefinition.builder(Id.of("custom invoker"), getEndpoints())
                .withInvoker(CustomInvoker.class)
                .build();
    }

    JTestDefinition withComment(){
        return JTestDefinition.builder(Id.of("comment"), getEndpoints())
                .withComment("definition with comment")
                .withQueryProvider(SINGLE_QUERY)
                .build();
    }

    JTestDefinition singleValidator(){
        return JTestDefinition.builder(Id.of("single validator"), getEndpoints())
                .withValidators(Collections.singletonList(NotNullResponseValidator.class))
                .withQueryProvider(SINGLE_QUERY)
                .build();
    }

    JTestDefinition listOfValidators(){
        return JTestDefinition.builder(Id.of("validators list"), getEndpoints())
                .withValidators(Arrays.asList(NotNullResponseValidator.class, TrueValidator.class))
                .withQueryProvider(SINGLE_QUERY)
                .build();
    }

    JTestDefinition allFields(){
        return JTestDefinition.builder(Id.of("all fields"), getEndpoints())
                .withQueryProvider(SINGLE_QUERY)
                // .withInvoker(CustomInvoker.class) // TODO JFG-1018
                .withValidators(Arrays.asList(NotNullResponseValidator.class, TrueValidator.class))
                .withComment("all fields definition")
                .build();
    }


}
