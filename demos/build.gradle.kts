plugins {
    application
    id("me.champeau.jmh") version "0.7.2"
}

group = "edu.boisestate.lowry.crypto"
version = "1.0"

repositories {
    mavenCentral()
}

application {
    mainClass.set("edu.boisestate.lowry.crypto.demo.CipherDemo")
}

dependencies {
    implementation(project(":lib"))
    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

jmh {
    warmupIterations.set(3)
    iterations.set(5)
    fork.set(1)

    resultFormat.set("CSV")
    resultsFile.set(project.file("build/reports/jmh/results.csv"))

    includeTests.set(false)
}
