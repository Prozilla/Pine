package dev.prozilla.pine;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

public class ConventionsPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.setGroup("dev.prozilla");
		
		project.getPluginManager().apply(JavaPlugin.class);
		
		project.getRepositories().mavenCentral();
		project.getRepositories().maven(repo -> repo.setUrl(project.uri("https://repo.gradle.org/gradle/libs-releases")));
		
		JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
		java.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(21));
		
		SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
		SourceSet main = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
		main.getResources().srcDir("src/main/resources");
		
		project.getTasks().named("processResources", task -> {
			if (task instanceof Copy copyTask) {
				copyTask.setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE);
			}
		});
	}
	
}