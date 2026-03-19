plugins {
    application
}

group = "edu.boisestate.lowry.crypto"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}
