plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
}

group = "dev.kaytea"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    { exclude("org.jetbrains.compose.material") }
    implementation("com.bybutter.compose:compose-jetbrains-expui-theme:2.0.0")
    implementation("com.squareup.okio:okio:3.3.0")
    implementation("com.charleskorn.kaml:kaml:0.54.0")
}

kotlin {
    jvmToolchain(18)
}

compose.desktop.application {
    mainClass = "dev.kaytea.taller.seis.MainKt"
}
