package com.example.petstore.deployment.function;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import software.amazon.awscdk.services.lambda.Code;

public class QuarkusMeta {

    public static final String QUARKUS_DEFAULT_LAMBDA_HANDLER =
            "io.quarkus.amazon.lambda.runtime.QuarkusStreamHandler::handleRequest";

    public static final int QUARKUS_DEFAULT_LAMBDA_MEMORY = 128; // todo: can be less

    @Produces
    @Singleton
    public Code quarkusCode() {
        return Code.fromAsset("../lambda/build/function.zip");
    }

}
