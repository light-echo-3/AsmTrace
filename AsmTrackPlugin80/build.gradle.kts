import java.util.Properties

plugins {
    id("java-gradle-plugin") //会自动引入java-library、gradleApi()
    id("maven-publish") //maven发布插件
    kotlin("jvm") version "1.8.10"//支持kotlin编写插件
//    id ("com.vanniktech.maven.publish") version "0.30.0"
    //自动发布到maven中央仓库插件
    //https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_gradle
    id ("org.jreleaser") version "1.17.0"

//    id("signing") // 添加签名插件

}

gradlePlugin {
    plugins {
        create("TracePlugin") {
//            group = "io.github.light-echo-3"
//            version = "3.0.3"
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



group = "io.github.light-echo-3"
//version = '1.0.0-SNAPSHOT'
version = "3.0.5"



//----------------------------------- publish begin -----------------------------------
java {
    withJavadocJar()
    withSourcesJar()
}
/**
 * 发布插件
 * 参考：https://docs.gradle.org/8.5/userguide/publishing_maven.html#publishing_maven:resolved_dependencies
 *
 * 发布为bom，参考：https://zhuanlan.zhihu.com/p/195678201
 * 注意：java-platform不能与java和java-library同时存在
 */
publishing {
    publications {
        create<MavenPublication>("mavenAsmTrace") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("AsmTrackPlugin")
                description.set("A Gradle plugin for method tracing using ASM")
                url.set("https://github.com/light-echo-3/AsmTrace")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("lightEcho")
                        name.set("lightEcho")
                        email.set("hudequan777@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/light-echo-3/AsmTrace.git")
                    developerConnection.set("scm:git:ssh://github.com:light-echo-3/AsmTrace.git")
                    url.set("https://github.com/light-echo-3/AsmTrace")
                }
            }

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }

    repositories {
        maven {
            name = "buildRepo"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}



//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            artifactId = "asmtrace"
//            from(components["java"])
//            versionMapping {
//                usage("java-api") {
//                    fromResolutionOf("runtimeClasspath")
//                }
//                usage("java-runtime") {
//                    fromResolutionResult()
//                }
//            }
//            pom {
//                name = "AndroidAsmTracePlugin"
//                description = "A android gradle plugin to trace method use asm impliment"
//                url = "https://github.com/light-echo-3/AsmTrace"
//                properties = mapOf(
//                    "myProp" to "value",
//                    "prop.with.dots" to "anotherValue"
//                )
//                licenses {
//                    license {
//                        name = "The Apache License, Version 2.0"
//                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
//                    }
//                }
//                developers {
//                    developer {
//                        id = "LightEcho3"
//                        name = "Light Echo"
//                        email = "hudequan777@gmail.com"
//                    }
//                }
//                scm {
//                    connection = "scm:git:git://example.com/my-library.git"
//                    developerConnection = "scm:git:ssh://example.com/my-library.git"
//                    url = "https://github.com/light-echo-3/AsmTrace"
//                }
//            }
//        }
//    }
//    repositories {
//        maven {
//            url = uri(layout.buildDirectory.dir("staging-deploy"))
//        }
//    }
//}

// jreleaser配置文件：~/.jreleaser/config.toml
/*
jreleaser:https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_gradle

JRELEASER_MAVENCENTRAL_USERNAME = "<your-publisher-portal-username>"
JRELEASER_MAVENCENTRAL_PASSWORD = "<your-publisher-portal-password>"
JRELEASER_NEXUS2_USERNAME = "<your-sonatype-account-username>"
JRELEASER_NEXUS2_PASSWORD = "<your-sonatype-account-password>"
JRELEASER_GPG_PASSPHRASE = "<your-pgp-passphrase>"
JRELEASER_GITHUB_TOKEN = "<your-github-token"
 */
jreleaser {
    gitRootSearch.set(true)
    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        armored.set(true)
        mode.set(org.jreleaser.model.Signing.Mode.FILE)
        publicKey.set("/Users/hudequan/shell/gpg/public.pgp")
        secretKey.set("/Users/hudequan/shell/gpg/private.pgp")
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("${layout.buildDirectory.get()}/staging-deploy")
                }
            }
        }
    }
}



//----------------------------------- publish end -----------------------------------

//publishing {
//    publications {
//        // 这里的 "helloAsm" 名字也可以随便起
//        create<MavenPublication>("helloAsm") {
//            groupId = "io.github.light-echo-3"
//            artifactId = "asmtrace"
//            version = "3.0.0"
//            from(components["java"])
//        }
//    }
//    repositories {
//
//        maven {
//            name = "mavenCenter"
////            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//            credentials {
//                username = localProperties.getProperty("SONATYPE_USERNAME")
//                password = localProperties.getProperty("SONATYPE_PASSWORD")
//                logger.warn("------username=$username,password=$password")
//            }
//        }
//
//        maven {
//            name = "centralManualTesting"
//            url = uri("https://central.sonatype.com/api/v1/publisher/deployments/download/")
//            credentials(HttpHeaderCredentials::class){
//                name = project.findProperty("centralManualTestingAuthHeaderName") as String
//                value = project.findProperty("centralManualTestingAuthHeaderValue") as String
//                logger.warn("------name=$name,password=$value")
//            }
//            authentication {
//                create<HttpHeaderAuthentication>("header")
//            }
//        }
//        mavenCentral()
//
//
//        maven {
//            name = "CurrenDirRepo"
//            url = uri("../repo")
//        }
//
//    }
//}

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