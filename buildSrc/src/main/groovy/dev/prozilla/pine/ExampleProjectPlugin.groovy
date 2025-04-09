package dev.prozilla.pine

import org.gradle.api.Plugin
import org.gradle.api.Project

class ExampleProjectPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.pluginManager.apply("java")
        project.pluginManager.apply("application")

        project.version = "1.0.0.0"

        project.dependencies {
            implementation project.project(":pine-engine")
        }

        project.tasks.register("export") {
            dependsOn("build")
            doLast {
                def buildToolTask = project.tasks.getByPath(":pine-engine:runBuildTool")
                buildToolTask.args = [project.projectDir.absolutePath]
                buildToolTask.exec()
            }
        }
    }

}
