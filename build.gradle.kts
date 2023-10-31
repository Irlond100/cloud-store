plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "ru.dude"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("io.jsonwebtoken:jjwt:0.2")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.1.0")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    runtimeOnly(group = "org.postgresql", name = "postgresql")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
