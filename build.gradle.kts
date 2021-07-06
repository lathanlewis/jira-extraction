import com.devfactory.buildscan.applyDefaults
import com.devfactory.buildscan.register

plugins {
    com.devfactory.`build-scan-values`
}

allprojects {
    group = "com.devfactory.eng"
}

buildScanValues {
    applyDefaults()
    register("project version") { _ ->
        value("version", project.version.toString())
    }
}

