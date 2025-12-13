package dev.prozilla.pine;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.UnknownTaskException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.plugins.ApplicationPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.internal.os.OperatingSystem;

import java.util.Collections;

public class ExamplesConventionsPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(JavaPlugin.class);
		project.getPluginManager().apply(ApplicationPlugin.class);
		
		project.setVersion("1.0.0.0");
		
		project.getDependencies().add("implementation", project.project(":pine-engine"));
		
		project.getTasks().register("export", (task) -> {
			task.dependsOn("build");
			task.doLast((t) -> {
				JavaExec runBuildTool = (JavaExec)project.getRootProject()
					.project(":pine-engine")
					.getTasks()
					.getByName("runBuildTool");
				
				runBuildTool.setArgs(Collections.singletonList(project.getProjectDir().getAbsolutePath()));
				runBuildTool.exec();
			});
			
			task.setDescription("Builds the application using Pine's build tool");
			task.setGroup("application");
		});
		
		project.afterEvaluate((p) -> {
			try {
				if (project.getTasks().getByName("start") instanceof JavaExec task) {
					FileCollection classpath = project.getExtensions()
						.getByType(SourceSetContainer.class)
						.getByName("main")
						.getRuntimeClasspath();
					task.setClasspath(classpath);
					
					if (OperatingSystem.current().isMacOsX()) {
						task.jvmArgs("-XstartOnFirstThread");
					}
					
					task.systemProperty("dev-mode", "true");
					task.setDescription("Starts the application");
					task.setGroup("application");
				}
			} catch (UnknownTaskException e) {
				System.out.println("Task 'start' was not found");
			}
		});
	}
	
}
