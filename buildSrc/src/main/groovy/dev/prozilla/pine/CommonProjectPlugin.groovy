package dev.prozilla.pine

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion

class CommonProjectPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.group = "dev.prozilla"

        project.pluginManager.apply("java")

        project.repositories {
            mavenCentral()
            maven {
                url = project.uri("https://repo.gradle.org/gradle/libs-releases")
            }
        }

        project.java {
            toolchain {
                languageVersion = JavaLanguageVersion.of(21)
            }
        }

        project.sourceSets {
            main {
                resources {
                    srcDir "src/main/resources"
                }
            }
        }

        project.tasks.named("processResources") {
            it.duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
        }
    }

}
