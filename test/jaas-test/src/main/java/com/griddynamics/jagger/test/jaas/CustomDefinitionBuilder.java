package com.griddynamics.jagger.test.jaas;

import com.griddynamics.jagger.engine.e1.collector.NotNullResponseValidator;
import com.griddynamics.jagger.engine.e1.collector.ResponseValidator;
import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import com.griddynamics.jagger.test.jaas.validator.ResponseStatusValidator;
import com.griddynamics.jagger.user.test.configurations.JTestDefinition;
import com.griddynamics.jagger.user.test.configurations.auxiliary.Id;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by oskliarov on 12/6/16.
 */
public class CustomDefinitionBuilder {
    private final List<Class<? extends ResponseValidator>> validators;
    private String id;
    private Iterable queryProvider;

    public CustomDefinitionBuilder(String id,  Iterable queryProvider) {
        this.id = id;
        this.queryProvider = queryProvider;
        this.validators = Arrays.asList(NotNullResponseValidator.class, ResponseStatusValidator.class);
    }

    public CustomDefinitionBuilder addValidator(Class<? extends ResponseValidator> validator){
        validators.add(validator);
        return this;
    }

    public JTestDefinition build(){
        return JTestDefinition.builder(Id.of(id), getEndpoints())
                .withQueryProvider(queryProvider)
                .withValidators(validators).build();
                //withMetrics(metric-success-rate, metric-not-null-response)
    }

    public static Iterable<JHttpEndpoint> getEndpoints() {
        // TODO "${jaas.endpoint}"
        return Collections.singletonList(new JHttpEndpoint("http://localhost:8088"));
    }

}
