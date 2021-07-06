package com.example.petstore.deployment;

import com.devfactory.cdk.base.BaseEnvironment;
import lombok.Getter;

import javax.inject.Singleton;

@Singleton
public class Environment extends BaseEnvironment {
    @Getter public final String shortName = "eng";
}
