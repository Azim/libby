import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;

val mainClassPath = "com.alessiodp.libby.StandaloneTestMain"

plugins {
    id("net.kyori.blossom") version "2.0.1"
    id("com.gradleup.shadow") version "9.3.0"
}

dependencies {
    api(project(":libby-core"))
    testCompileOnly("org.apache.commons:commons-lang3:3.13.0")
    testImplementation("commons-io:commons-io:2.14.0")
}

val shadowTestJar = tasks.create<ShadowJar>("shadowTestJar") {
    archiveClassifier.set("tests")
    from(sourceSets.test.get().output, sourceSets.main.get().output)
    manifest {
        attributes["Main-Class"] = mainClassPath
    }
    configurations = listOf(project.configurations.compileClasspath.get())
}

val downloadDirectory = layout.buildDirectory.asFile.get().absolutePath.replace("\\", "\\\\")
val testJarFile = shadowTestJar.archiveFile.get().asFile.absolutePath.replace("\\", "\\\\")

tasks.test {
    dependsOn(shadowTestJar)
}

sourceSets {
    test {
        blossom {
            javaSources {
                property("buildDir", downloadDirectory)
                property("testJar", testJarFile)
            }
        }
    }
}
