package com.devfactory.eng

import org.gradle.api.Project

object Artifacts {

    object AWS {
        const val urlConnectionClient = "software.amazon.awssdk:url-connection-client:2.15.24"
        object Dynamodb {
            const val enhanced = "software.amazon.awssdk:dynamodb-enhanced:2.15.24"
        }
    }
    object JUnit {
        const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:5.7.0"
        const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine"
        const val mockitoCore = "org.mockito:mockito-core:3.6.28"
        const val assertJCore = "org.assertj:assertj-core:3.18.1"
        const val restAssured = "io.rest-assured:rest-assured:4.3.3"
    }

    object CDK {
        const val base = "com.devfactory.cdk:cdk-base:0.1.11"

        val core = cdkArtifact("core")
        val lambda = cdkArtifact("lambda")
        val dynamodb = cdkArtifact("dynamodb")
        val appsync = cdkArtifact("appsync")

        private fun cdkArtifact(name: String, version: String = "1.82.0") = "software.amazon.awscdk:$name:$version"
    }

    object Quarkus {
        const val platform = "io.quarkus:quarkus-universe-bom:1.10.3.Final"

        val yamlConfig = quarkusArtifact("config-yaml")

        val restEasy = quarkusArtifact("resteasy")
        val restEasyJackson = quarkusArtifact("resteasy-jackson")
        val amazonDynamodb = quarkusArtifact("amazon-dynamodb")

        val lambda = quarkusArtifact("amazon-lambda")

        val junit5 = quarkusArtifact("junit5")
        val junit5Mock = quarkusArtifact("junit5-mockito")
        val lambdaTest = quarkusArtifact("test-amazon-lambda")

        private fun quarkusArtifact(name: String) = "io.quarkus:quarkus-$name"
    }
}

val Project.lambdaModule: Project get() = project(":lambda")
