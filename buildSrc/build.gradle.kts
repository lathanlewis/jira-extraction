import java.net.URI

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    jcenter()
    gradlePluginPortal()

    if (System.getenv("GITHUB_REPOSITORY") != null && System.getenv("GITHUB_RUN_ID") != null) {
        trilogyGroupGithub("devfactory-structurizr-dsl")
        trilogyGroupGithub("devfactory-deployment-gradle-plugin")
    } else {
        maven("https://nexus.devfactory.com/nexus/content/repositories/releases")
    }
}

dependencies {
    implementation(devfactoryPlugin("eng.eng-project"))
    implementation(devfactoryPlugin("eng.java-module"))

    api(devfactoryPlugin("build-scan-values"))

    implementation("io.freefair.gradle:lombok-plugin:5.3.0")
    implementation(plugin("com.gradle.enterprise", "3.5"))

    implementation(plugin("io.quarkus", "1.10.3.Final"))
    implementation(plugin("com.github.node-gradle.node", "2.2.3"))
}

fun plugin(name: String, version: String): String {
    return "$name:$name.gradle.plugin:$version"
}

fun devfactoryPlugin(name: String, version: String = "0.2.78"): String {
    return plugin("com.devfactory.$name", version)
}

fun RepositoryHandler.trilogyGroupGithub(repository: String) {
    maven {
        name = "GitHubPackages-${repository}"
        url = URI("https://maven.pkg.github.com/trilogy-group/$repository")
        credentials {
            username = ""
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
