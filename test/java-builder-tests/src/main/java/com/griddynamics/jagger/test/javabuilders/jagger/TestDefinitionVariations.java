package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.invoker.v2.JHttpQuery;
import com.griddynamics.jagger.test.javabuilders.config.JaggerPropertiesProvider;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestDefinitionVariations {

    private static final List<JHttpQuery> SINGLE_QUERY = Collections.singletonList(new JHttpQuery().get().path("/sleep/10"));
    private JaggerPropertiesProvider properties;

    TestDefinitionVariations(JaggerPropertiesProvider properties) {
        this.properties = properties;
    }

    private Iterable<JHttpEndpoint> getEndpoints() {
        return Collections.singletonList(new JHttpEndpoint(properties.getPropertyValue("test.endpoint.url")));
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
        // create query with different latency to test query rotation
        Iterable<JHttpQuery> queries = Stream.of("100", "50", "25")
                .map(q -> new JHttpQuery().get().path("/sleep", q))
                .collect(Collectors.toList());
        return JTestDefinition.builder(Id.of("queries list"), getEndpoints())
                .withQueryProvider(queries)
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
                .build();
    }

    JTestDefinition singleValidator(){
        return JTestDefinition.builder(Id.of("single validator"), getEndpoints())
                .withValidators(Collections.singletonList(NotNullResponseValidator.class))
                .build();
    }

    JTestDefinition listOfValidators(){
        return JTestDefinition.builder(Id.of("validators list"), getEndpoints())
                .withValidators(Arrays.asList(NotNullResponseValidator.class, TrueValidator.class))
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
