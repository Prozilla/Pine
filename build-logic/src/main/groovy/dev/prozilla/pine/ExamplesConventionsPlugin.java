package dev.prozilla.pine;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ApplicationPlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.JavaExec;

import java.util.Collections;

public class ExamplesConventionsPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(JavaPlugin.class);
		project.getPluginManager().apply(ApplicationPlugin.class);
		
		project.setVersion("1.0.0.0");
		
		project.getDependencies().add("implementation", project.project(":pine-engine"));
		
		project.getTasks().register("export", task -> {
			task.dependsOn("build");
			task.doLast(t -> {
				JavaExec runBuildTool = (JavaExec) project.getRootProject()
					.project(":pine-engine")
					.getTasks()
					.getByName("runBuildTool");
				
				runBuildTool.setArgs(Collections.singletonList(project.getProjectDir().getAbsolutePath()));
				runBuildTool.exec();
			});
		});
	}
	
}
