package com.example.petstore.deployment;

import com.example.petstore.deployment.function.FunctionFactory;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import software.amazon.awscdk.services.lambda.Function;

public class Functions {

    @Singleton
    @Produces
    public Function findPersonsFunction(FunctionFactory functionFactory) {
        return functionFactory.createQuarkusFunction("find-persons");
    }

    @Singleton
    @Produces
    public Function createPetFunction(FunctionFactory functionFactory) {
        return functionFactory.createQuarkusFunction("create-pet");
    }

}
