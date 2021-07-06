package com.example.petstore;

import com.devfactory.base.BaseEnvironment;
import lombok.Getter;

import javax.inject.Singleton;

@Singleton
public class Environment extends BaseEnvironment {
    @Getter public final String shortName = "pets";
}
