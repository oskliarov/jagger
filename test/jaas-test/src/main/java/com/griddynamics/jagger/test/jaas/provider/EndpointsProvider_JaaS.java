package com.griddynamics.jagger.test.jaas.provider;

import com.griddynamics.jagger.invoker.v2.JHttpEndpoint;
import org.springframework.beans.factory.annotation.Value;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides main endpoint of JaaS.
 */
public class EndpointsProvider_JaaS implements Iterable {
    private List<JHttpEndpoint> endpoints = new LinkedList<>();

    @Value( "${jaas.endpoint}" )
    protected String endpoint;

    public EndpointsProvider_JaaS() {}

    @Override
    public Iterator iterator() {
        if (endpoints.isEmpty()) {
            endpoints.add(new JHttpEndpoint(endpoint));
        }

        return endpoints.iterator();
    }
}