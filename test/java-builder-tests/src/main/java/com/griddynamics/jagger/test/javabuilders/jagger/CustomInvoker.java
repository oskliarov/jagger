package com.griddynamics.jagger.test.javabuilders.jagger;

import com.griddynamics.jagger.invoker.InvocationException;
import com.griddynamics.jagger.invoker.Invoker;

public class CustomInvoker implements Invoker {
    @Override
    public Object invoke(Object query, Object endpoint) throws InvocationException {
        return null;
    }
}
