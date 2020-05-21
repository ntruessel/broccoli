val jooqVersion: String by project
val micronautVersion: String by project

plugins {
    id("application")
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

buildscript {
    val jooqVersion: String by project

    dependencies {
        classpath("org.jooq:jooq-codegen:${jooqVersion}")
        classpath("org.jooq:jooq-meta:${jooqVersion}")
        classpath("org.jooq:jooq-meta-extensions:${jooqVersion}")
    }
}

group = "ch.ntruessel.broccoli"
version = "0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("ch.ntruessel.broccoli.server.BroccoliServer")
}

dependencies {
    kapt(platform("io.micronaut:micronaut-bom:${micronautVersion}"))
    kapt("io.micronaut:micronaut-inject-java")
    implementation(platform("io.micronaut:micronaut-bom:${micronautVersion}"))
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.micronaut.configuration:micronaut-jdbc-hikari")
    implementation("io.micronaut.configuration:micronaut-jooq")
    implementation("io.micronaut.configuration:micronaut-liquibase")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    runtimeOnly("com.h2database:h2:1.4.200")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn("generateMetamodel")
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xinline-classes")
        jvmTarget = "11"
    }
}

task("generateMetamodel") {
    inputs.files("${projectDir}/src/main/resources/db/migrations/")
    outputs.dir("${projectDir}/src/main/java")
    doLast {
        val configuration = org.jooq.meta.jaxb.Configuration()
                .withBasedir(projectDir.absolutePath)
                .withGenerator(org.jooq.meta.jaxb.Generator()
                        .withTarget(org.jooq.meta.jaxb.Target()
                                .withDirectory("src/main/java")
                                .withPackageName("ch.ntruessel.broccoli.server.jooq"))
                        .withDatabase(org.jooq.meta.jaxb.Database()
                                .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
                                .withProperties(
                                        org.jooq.meta.jaxb.Property()
                                                .withKey("scripts")
                                                .withValue("src/main/resources/db/migrations/changelog.yml"),
                                        org.jooq.meta.jaxb.Property()
                                                .withKey("includeLiquibaseTables")
                                                .withValue("false"))))
        org.jooq.codegen.GenerationTool.generate(configuration)
    }
}
