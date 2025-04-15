plugins {
    id("java-gradle-plugin") //会自动引入java-library、gradleApi()
    id("maven-publish") //maven发布插件
    kotlin("jvm") version "1.8.10"//支持kotlin编写插件
}

gradlePlugin {
    plugins {
        create("TracePlugin") {
            group = "plugin.asm.track"
            version = "3.0.0"
            id = "asm.track.id"
            implementationClass = "com.wuzhu.asmtrack.AsmTrackPlugin"
        }
    }
}


group = "plugin.asm.track"
version = "3.0.0"


publishing {
    publications {
        // 这里的 "helloAsm" 名字也可以随便起
        create<MavenPublication>("helloAsm") {
            groupId = "plugin.asm.track"
            artifactId = "asmtrack"
            version = "3.0.0"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "CurrenDirRepo"
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