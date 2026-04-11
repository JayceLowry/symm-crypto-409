import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
}

val javaVersion = JavaLanguageVersion.of(21)

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(javaVersion)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.withType<Javadoc>().configureEach {
    options {
        val stp = this as StandardJavadocDocletOptions
        stp.memberLevel = JavadocMemberLevel.PRIVATE
        stp.addBooleanOption("html5", true)
        stp.windowTitle = "Cryptographic Protocol Internals"
        stp.docTitle = "Implementation Reference"
        stp.linkSource(true)

        stp.encoding = "UTF-8"
        val versionPath = javaVersion.asInt()
        stp.links("https://docs.oracle.com/en/java/javase/$versionPath/docs/api/")
        stp.addFileOption("-add-stylesheet", file("${projectDir}/src/main/resources/style.css"))
        stp.tags(
            "implNote:a:Implementation Note:"
        )
    }
}
