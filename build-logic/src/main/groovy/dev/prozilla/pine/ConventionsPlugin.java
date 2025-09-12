package dev.prozilla.pine;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

import java.util.List;

public class ConventionsPlugin implements Plugin<Project> {
	
	public static final String GROUP = "dev.prozilla";
	public static final int JAVA_VERSION = 21;
	public static final String RESOURCES_PATH = "src/main/resources";
	public static final List<String> COMPILER_ARGS = List.of("-Xlint:deprecation", "-Xlint:cast", "-Xlint:finally");
	
	@Override
	public void apply(Project project) {
		project.setGroup(GROUP);
		
		project.getPluginManager().apply(JavaPlugin.class);
		
		project.getRepositories().mavenCentral();
		project.getRepositories().maven((repo) -> repo.setUrl(project.uri("https://repo.gradle.org/gradle/libs-releases")));
		
		JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
		java.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(JAVA_VERSION));
		
		SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);
		SourceSet main = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
		main.getResources().srcDir(RESOURCES_PATH);
		
		project.getTasks().named("processResources", (task) -> {
			if (task instanceof Copy copyTask) {
				copyTask.setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE);
			}
		});
		
		project.getTasks().withType(JavaCompile.class, (task) -> task.getOptions().getCompilerArgs().addAll(COMPILER_ARGS));
	}
	
}