import com.devfactory.eng.Artifacts
import com.devfactory.eng.lambdaModule
import com.moowork.gradle.node.npm.NpxTask

plugins {
    application
    com.devfactory.eng.`java-module`
    com.github.`node-gradle`.node
}

application {
    mainClass.set("com.devfactory.cdk.base.Main")
    // Weld does not yet support new reflection constraints and triggers warnings
    applicationDefaultJvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
}

node {
    version = "12.18.0"
    npmVersion = "6.14.5"
    yarnVersion = "1.22.10"
    download = true
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://nexus.devfactory.com/nexus/content/repositories/releases")
}

dependencies {
    implementation(Artifacts.CDK.base)
    implementation(Artifacts.CDK.core)
    implementation(Artifacts.CDK.lambda)
    implementation(Artifacts.CDK.dynamodb)
    implementation(Artifacts.CDK.appsync)
    implementation(lambdaModule)

    testImplementation(Artifacts.JUnit.jupiterApi)
    testImplementation(Artifacts.JUnit.mockitoCore)
    testImplementation(Artifacts.JUnit.assertJCore)
}

// Weld expects a classpath entry with beans to contain beans.xml,
// but Gradle splits classes and resources into separate entries, so
// we move beans.xml into classes

sourceSets.main {
    resources {
        exclude("META-INF/beans.xml")
    }
}

tasks {
    val copyBeansXml = register<Copy>("copyBeansXml") {
        from("${projectDir}/src/main/resources") {
            include("META-INF/beans.xml")
        }
        into("${buildDir}/classes/java/main")
    }

    processResources {
        dependsOn(copyBeansXml)
    }

    // todo: introduce to gradle plugin
    val cdkCleanup = register<Delete>("cdkCleanup") {
        group = "deployment"
        delete(fileTree(buildDir.resolve("cdk.out")))
        doLast {
            buildDir.resolve("cdk.out").delete()
        }
    }
    register<NpxTask>("cdkDeploy") {
        group = "deployment"
        command = "cdk"
        setEnvironment(mapOf("STAGE" to "dev"))
        setArgs(listOf("deploy", "--require-approval", "never", "--all", "-c", "stage=dev"))
        dependsOn("installDist")
        dependsOn(assemble, ":lambda:assemble")
    }
}

