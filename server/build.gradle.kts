object Versions {
    const val jooq = "3.13.2"
}

plugins {
    id("org.springframework.boot") version "2.2.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
}

buildscript {
    dependencies {
        classpath("org.jooq:jooq-codegen:3.13.2")
        classpath("org.jooq:jooq-meta:3.13.2")
        classpath("org.jooq:jooq-meta-extensions:3.13.2")
    }
}

group = "ch.ntruessel.broccoli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:${Versions.jooq}")
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.liquibase:liquibase-core:3.9.0")
    runtimeOnly("com.h2database:h2:1.4.200")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
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
                                            .withValue("false"))))
    org.jooq.codegen.GenerationTool.generate(configuration)
}
