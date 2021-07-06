package com.devfactory.base;

import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class NamingStrategy {

    private final BaseEnvironment environment;

    public String name(String resourceName) {
	return String.format("%s-%s-%s", environment.getShortName(), resourceName, environment.getStage());
    }

}
