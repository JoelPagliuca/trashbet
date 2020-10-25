plugins {
    val kotlin_version = "1.4.0"

    application
    kotlin("jvm") version kotlin_version
    kotlin("plugin.serialization") version kotlin_version
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val ktor_version = "1.4.1"

    // kotlin
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")

    // ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // test
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClass.set("trashbet.AppKt")
}
