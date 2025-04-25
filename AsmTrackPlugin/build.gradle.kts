import java.util.Properties

plugins {
    id("java-gradle-plugin") //会自动引入java-library、gradleApi()
    id("maven-publish") //maven发布插件
    kotlin("jvm") version "1.8.10"//支持kotlin编写插件
    //自动发布到maven中央仓库插件
    //https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_gradle
//    id ("org.jreleaser") version "1.17.0"
}

gradlePlugin {
    plugins {
        create("TracePlugin") {
            id = "asm.trace.id"
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


//----------------------------------- publish begin -----------------------------------

group = "io.github.light-echo-3"
//version = "1.0.0-SNAPSHOT"
version = "2.0.0"

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
            artifactId = "asmtrace" //project.name
            version = project.version.toString()

            from(components["java"])

            pom {
                name.set("AsmTracsPlugin")
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
                        id.set("lightEcho3Id")
                        name.set("lightEcho3")
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

        maven {
            name = "CurrenDirRepo"
            url = uri("../repo")
        }

    }
}





//----------------------------------- publish end -----------------------------------

dependencies {
//    implementation("com.android.tools.build:gradle:8.0.0")
    implementation("com.android.tools.build:gradle:7.1.2")
    implementation("commons-io:commons-io:2.11.0")
    implementation("commons-codec:commons-codec:1.15") // 或更高版本
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


//----------------------------------- jreleaser begin -----------------------------------

//// jreleaser配置文件：~/.jreleaser/config.toml
///*
//jreleaser:https://jreleaser.org/guide/latest/examples/maven/maven-central.html#_gradle
//
//JRELEASER_MAVENCENTRAL_USERNAME = "<your-publisher-portal-username>"
//JRELEASER_MAVENCENTRAL_PASSWORD = "<your-publisher-portal-password>"
//JRELEASER_NEXUS2_USERNAME = "<your-sonatype-account-username>"
//JRELEASER_NEXUS2_PASSWORD = "<your-sonatype-account-password>"
//JRELEASER_GPG_PASSPHRASE = "<your-pgp-passphrase>"
//JRELEASER_GITHUB_TOKEN = "<your-github-token"
// */
//jreleaser {
//    gitRootSearch.set(true)
//    signing {
//        active.set(org.jreleaser.model.Active.ALWAYS)
//        armored.set(true)
//        mode.set(org.jreleaser.model.Signing.Mode.FILE)
//        publicKey.set("/Users/hudequan/shell/gpg/public.pgp")
//        secretKey.set("/Users/hudequan/shell/gpg/private.pgp")
//    }
//
//    deploy {
//        maven {
//            //尝试发布snapshot，失败
//            //bug:https://github.com/jreleaser/jreleaser/issues
////            mavenCentral {
////                create("sonatype") {
////                    snapshotSupported = true
////                    active = org.jreleaser.model.Active.ALWAYS
////                    url = "https://central.sonatype.com/repository/maven-snapshots"
////                    stagingRepository("${layout.buildDirectory.get()}/staging-deploy")
////                }
////            }
//            mavenCentral {
//                create("sonatype") {
//                    active.set(org.jreleaser.model.Active.ALWAYS)
//                    url.set("https://central.sonatype.com/api/v1/publisher")
//                    stagingRepository("${layout.buildDirectory.get()}/staging-deploy")
//                }
//            }
//        }
//    }
//}


//----------------------------------- jreleaser end -----------------------------------