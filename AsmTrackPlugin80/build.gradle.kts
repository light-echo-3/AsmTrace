plugins {
    id("java-gradle-plugin") //会自动引入java-library、gradleApi()
    id("maven-publish") //maven发布插件
    kotlin("jvm") version "1.8.10"//支持kotlin编写插件
}

gradlePlugin {
    plugins {
        create("routerPlugin") {
            group = "plugin.asm.track"
            version = "2.0.0"
            id = "test.asm.track.id"
            implementationClass = "com.wuzhu.asmtrack.AsmTrackPlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("../repo")
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.0.0")
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}
kotlin {
    jvmToolchain(17)
}