import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

group = "challenge.com"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile("io.javalin:javalin:2.6.0")
    compile("org.slf4j:slf4j-simple:1.7.25")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.8")
    compile("org.koin:koin-core:1.0.2")
    compile("com.h2database:h2:1.+")
    compile("com.zaxxer:HikariCP:3.+")
    compile("org.jetbrains.exposed:exposed:0.11.+")
    compile(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}