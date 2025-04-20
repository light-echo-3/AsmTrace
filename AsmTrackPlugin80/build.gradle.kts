import java.util.Properties

plugins {
    id("java-gradle-plugin") //会自动引入java-library、gradleApi()
    id("maven-publish") //maven发布插件
    kotlin("jvm") version "1.8.10"//支持kotlin编写插件
}

gradlePlugin {
    plugins {
        create("TracePlugin") {
            group = "io.github.light-echo-3"
            version = "3.0.0"
            id = "asm.track.id"
            implementationClass = "com.wuzhu.asmtrack.AsmTrackPlugin"
        }
    }
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

publishing {
    publications {
        // 这里的 "helloAsm" 名字也可以随便起
        create<MavenPublication>("helloAsm") {
            groupId = "io.github.light-echo-3"
            artifactId = "asmtrace"
            version = "3.0.0"
            from(components["java"])
        }
    }
    repositories {

        maven {
            name = "mavenCenter"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = localProperties.getProperty("SONATYPE_USERNAME")
                password = localProperties.getProperty("SONATYPE_PASSWORD")
                logger.warn("------username=$username,password=$password")
            }
        }

        maven {
            name = "centralManualTesting"
            url = uri("https://central.sonatype.com/api/v1/publisher/deployments/download/")
            credentials(HttpHeaderCredentials::class){
                name = project.findProperty("centralManualTestingAuthHeaderName") as String
                value = project.findProperty("centralManualTestingAuthHeaderValue") as String
                logger.warn("------name=$name,password=$value")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        mavenCentral()


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