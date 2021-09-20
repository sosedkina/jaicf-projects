plugins {
    application
    kotlin("jvm") version "1.4.21"
    id("com.justai.jaicf.jaicp-build-plugin") version "0.1.1"
}

group = "com.justai.jaicf"
version = "1.0.0"

val jaicf = "1.2.0"
val logback = "1.2.6"
val hoplite = "1.4.7"
val jackson = "2.12.5"
val ktor = "1.5.1"

// Main class to run application on heroku. Either JaicpPollerKt, or JaicpServerKt. Will propagate to .jar main class.
application {
    mainClassName = "com.justai.jaicf.template.TemplateBotKt"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:$logback")

    implementation("com.just-ai.jaicf:core:$jaicf")
    implementation("com.just-ai.jaicf:jaicp:$jaicf")
    implementation("com.just-ai.jaicf:caila:$jaicf")

    implementation("com.sksamuel.hoplite:hoplite-core:$hoplite")
    implementation("com.sksamuel.hoplite:hoplite-yaml:$hoplite")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson")
    implementation("io.ktor:ktor-client-jackson:$ktor")

    implementation("edu.stanford.nlp:stanford-corenlp:4.2.2")
    implementation("edu.stanford.nlp:stanford-corenlp:4.2.2:models")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.create("stage") {
    dependsOn("shadowJar")
}

tasks.withType<com.justai.jaicf.plugins.jaicp.build.JaicpBuild> {
    mainClassName.set(application.mainClassName)
}
