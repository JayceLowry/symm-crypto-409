plugins {
    application
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
}
