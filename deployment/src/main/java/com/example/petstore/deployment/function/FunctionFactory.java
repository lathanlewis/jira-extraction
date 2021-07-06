package com.example.petstore.deployment.function;

import com.devfactory.cdk.base.NameStrategy;
import com.devfactory.cdk.base.stack.ExecutionStack;
import com.example.petstore.deployment.Environment;
import lombok.RequiredArgsConstructor;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.iam.PolicyStatementProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FunctionFactory {

    private final ExecutionStack executionStack;
    private final NameStrategy nameStrategy;
    private final Code quarkusCode;
    private final IRole defaultFunctionRole;
    private final Environment environment;

    public Function createQuarkusFunction(String name) {
        String fullName = String.format("%s-func", name);

        Function function = new Function(executionStack, nameStrategy.formatName(fullName), FunctionProps.builder()
                .functionName(nameStrategy.formatName(fullName))
                .code(quarkusCode)
                .handler(QuarkusMeta.QUARKUS_DEFAULT_LAMBDA_HANDLER)
                .memorySize(QuarkusMeta.QUARKUS_DEFAULT_LAMBDA_MEMORY)
                .role(defaultFunctionRole)
                .environment(Map.of(
                        "STAGE", environment.getStage(),
                        "QUARKUS_LAMBDA_HANDLER", name,
                        // workaround for https://github.com/oracle/graal/issues/841
                        "DISABLE_SIGNAL_HANDLERS", "true"
                ))
                .runtime(Runtime.PROVIDED)
                .build());

        function.addToRolePolicy(new PolicyStatement(PolicyStatementProps.builder()
                .resources(List.of("arn:aws:logs:*:*:*"))
                .actions(List.of("logs:*"))
                .build()));
        function.addToRolePolicy(new PolicyStatement(PolicyStatementProps.builder()
                .resources(List.of("*"))
                .actions(List.of("dynamodb:*"))
                .build()));

        return function;
    }

}
