plugins {
    `java-library`
    `maven-publish`
    signing
}

allprojects {
    group = "com.alessiodp.libby"
    version = "2.0.2-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    dependencies {
        compileOnly("org.jetbrains:annotations:24.0.1")
        testCompileOnly("org.jetbrains:annotations:24.0.1")
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withJavadocJar()
        withSourcesJar()
    }

    tasks.test {
        useJUnitPlatform()
    }

    publishing {
        repositories {
            /*
            maven {
                val releaseUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

                credentials {
                    username = (project.properties["ossrhUsername"] ?: "").toString()
                    password = (project.properties["ossrhPassword"] ?: "").toString()
                }
            }

            maven {
                val releaseUrl = "https://repo.alessiodp.com/releases"
                val snapshotUrl = "https://repo.alessiodp.com/snapshots"

                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

                credentials {
                    username = (project.properties["alessiodpRepoUsername"] ?: "").toString()
                    password = (project.properties["alessiodpRepoPassword"] ?: "").toString()
                }
            }
            */

            maven {
                val releaseUrl = "https://maven.hytalemodding.dev/releases"
                val snapshotUrl = "https://maven.hytalemodding.dev/snapshots"

                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotUrl else releaseUrl)

                credentials {
                    username = (project.properties["hytalemoddingRepoUsername"] ?: "").toString()
                    password = (project.properties["hytalemoddingRepoPassword"] ?: "").toString()
                }
            }
        }

        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                pom {
                    name.set("Libby")
                    description.set("A runtime dependency management library for plugins running in Java-based Minecraft server platforms.")
                    url.set("https://github.com/Azim/libby")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/license/mit/")
                        }
                    }

                    developers {
                        developer {
                            id = "AlessioDP"
                            email = "me@alessiodp.com"
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com/Azim/libby.git"
                        developerConnection = "scm:git:git@github.com:Azim/libby.git"
                        url = "https://github.com/Azim/libby"
                    }
                }
            }
        }
    }

    signing {
        setRequired {
            gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
        }
        useGpgCmd()
        sign(publishing.publications["mavenJava"])
    }
}