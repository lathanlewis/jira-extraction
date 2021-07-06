plugins {
    id("com.gradle.enterprise") version "3.5"
}

rootProject.name = "eng-template"

include("lambda")
include("deployment")

fun isGhActions(): Boolean = System.getenv("GITHUB_ACTIONS").isNullOrBlank().not()
fun isJenkins(): Boolean = System.getenv("JENKINS_URL").isNullOrBlank().not()
fun isCi(): Boolean = isGhActions() || isJenkins()

gradleEnterprise {
    server = "https://54.147.106.183/"
    buildScan {
        publishAlways()
        allowUntrustedServer = true
        isCaptureTaskInputFiles = true
    }
}

buildCache {
    local {
        isEnabled = !isCi()
    }
    remote(HttpBuildCache::class) {
        isEnabled = true
        isPush = isCi()
        setUrl("https://54.147.106.183/cache/")
        isAllowUntrustedServer = true
    }
}
