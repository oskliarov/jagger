package com.griddynamics.jagger.test.javabuilders.config;

import com.griddynamics.jagger.util.JaggerXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class JaggerPropertiesProvider {
    @Autowired
    private ApplicationContext context;

    public String getPropertyValue(String key){
        return getContext().getEnvironmentProperties().getProperty(key);
    }

    private JaggerXmlApplicationContext getContext() {
        return (JaggerXmlApplicationContext) context;
    }


}
