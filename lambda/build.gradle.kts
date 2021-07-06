import com.devfactory.eng.Artifacts

plugins {
    com.devfactory.eng.`java-module`
    io.quarkus
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(enforcedPlatform(Artifacts.Quarkus.platform))
    implementation(Artifacts.Quarkus.restEasy)
    implementation(Artifacts.Quarkus.restEasyJackson)
    implementation(Artifacts.Quarkus.lambda)
    implementation(Artifacts.Quarkus.yamlConfig)
    implementation(Artifacts.Quarkus.amazonDynamodb)

    testImplementation(Artifacts.JUnit.assertJCore)
    testImplementation(Artifacts.Quarkus.junit5)
    testImplementation(Artifacts.Quarkus.junit5Mock)
    testImplementation(Artifacts.Quarkus.lambdaTest)
}
