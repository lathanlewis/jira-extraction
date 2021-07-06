package com.example.petstore.deployment;

import com.devfactory.cdk.base.NameStrategy;
import com.devfactory.cdk.base.stack.ExecutionStack;
import software.amazon.awscdk.services.iam.*;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import java.util.List;

public class Roles {

    @Produces
    @Singleton
    public IRole defaultLambdaRole(ExecutionStack executionStack, NameStrategy nameStrategy) {
        return new Role(executionStack, nameStrategy.formatName("default-function-role"),
                RoleProps.builder()
                        .assumedBy(new ServicePrincipal("lambda.amazonaws.com"))
                        .managedPolicies(List.of(
                                ManagedPolicy.fromAwsManagedPolicyName("service-role/AWSLambdaBasicExecutionRole")))
                        .build());
    }

}
