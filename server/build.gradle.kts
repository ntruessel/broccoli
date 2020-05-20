val jooqVersion: String by project

plugins {
    id("org.springframework.boot") version "2.2.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:${jooqVersion}")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.liquibase:liquibase-core:3.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    implementation(kotlin("stdlib-jdk8"))
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
    val configuration = org.jooq.meta.jaxb.Configuration()
            .withGenerator(org.jooq.meta.jaxb.Generator()
                    .withDatabase(org.jooq.meta.jaxb.Database()
                            .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
                            .withProperties(
                                    org.jooq.meta.jaxb.Property()
                                            .withKey("scripts")
                                            .withValue("src/main/resources/db/migrations/changelog.yml"),
                                    org.jooq.meta.jaxb.Property()
                                            .withKey("includeLiquibaseTables")
                                            .withValue("false")))
                    .withTarget(org.jooq.meta.jaxb.Target()
                            .withDirectory("src/main/java/")
                            .withPackageName("ch.ntruessel.broccoli.server.jooq")))
    org.jooq.codegen.GenerationTool.generate(configuration)
}
