package dev.prozilla.pine.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.logging.handler.StandardErrorLogHandler;
import dev.prozilla.pine.common.logging.handler.StandardOutputLogHandler;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.common.system.FileSystem;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.Objects;

/**
 * Tool for building games made with the Pine engine.
 */
public class BuildTool {
	
	// Files and folders
	private static final String BUILD_PATH = "build/pine";
	private static final String LAUNCH4J_TEMP_PATH = "launch4j";
	private static final String CONFIG_NAME = "pine-config.json";
	
	// Debugging
	private static final boolean DEBUG_LAUNCH4J = true;
	
	private static final Logger logger = new Logger(new StandardOutputLogHandler(), new StandardOutputLogHandler())
		.setPrefix(Ansi.green(Logger.formatBadge("build")));
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException("Please specify the path to the game directory.");
		}
		
		try {
			run(args);
		} catch (Exception e) {
			logger.error("Build failed", e);
		}
	}
	
	private static void run(String[] args) throws Exception {
		// Check project directory
		Path projectDir = Paths.get(args[0]).toAbsolutePath().normalize();
		if (!Files.isDirectory(projectDir)) {
			throw new IllegalArgumentException("The specified path is not a directory: " + projectDir);
		}
		
		logger.logPath("Working in project directory", projectDir.toString());
		
		// Load configuration
		logger.log("Loading configuration file...");
		Path configPath = projectDir.resolve(CONFIG_NAME);
		if (!Files.exists(configPath)) {
			throw new IllegalArgumentException(String.format("Configuration file %s not found.", CONFIG_NAME));
		}
		ObjectMapper mapper = new ObjectMapper();
		BuildConfig config = mapper.readValue(configPath.toFile(), BuildConfig.class);
		
		// Prepare build directory
		Path buildDir = Files.createDirectories(projectDir.resolve(BUILD_PATH));
		prepareBuildDir(buildDir);
		Path launch4jDir = Files.createDirectories(buildDir.resolve(LAUNCH4J_TEMP_PATH));
		
		buildShadowJar(config, projectDir);
		Path jrePath = downloadAndExtractJRE(config, buildDir);
		bundleResources(config, projectDir, buildDir);
		bundleMods(config, buildDir);
		
		Path launch4jConfigPath = generateLaunch4jConfig(config, projectDir, buildDir, jrePath, launch4jDir);
		runLaunch4j(launch4jConfigPath, launch4jDir);
		
		finalizeBuild(config, buildDir, launch4jDir);
		logger.logPath(Ansi.green("Build completed in"), buildDir.toAbsolutePath().toString());
	}
	
	private static void prepareBuildDir(Path buildDir) {
		logger.log("Clearing build directory...");
		for(File file: Objects.requireNonNull(buildDir.toFile().listFiles())) {
			if (!file.isDirectory() || !file.getName().equals("jre")) {
				file.delete();
			}
		}
	}
	
	private static void buildShadowJar(BuildConfig config, Path projectDir) {
		logger.log("Building shadow jar...");
		
		Path gradleDir = projectDir;
		
		// Try to find gradle directory
		for (int i = 0; i < 2; i++) {
			if (!Files.exists(gradleDir.resolve("settings.gradle")) && !Files.exists(gradleDir.resolve("settings.gradle.kts"))) {
				if (i < 1) {
					gradleDir = gradleDir.getParent();
				} else {
					throw new IllegalArgumentException("Project does not have a valid gradle setup.");
				}
			} else {
				break;
			}
		}
		
		GradleConnector connector = GradleConnector.newConnector()
	        .useGradleUserHomeDir(new File(System.getProperty("user.home"), ".gradle"))
	        .forProjectDirectory(gradleDir.toFile());
		
		try (ProjectConnection connection = connector.connect()) {
			// Run shadowJar task
			connection.newBuild()
				.forTasks("shadowJar")
				.withArguments("--stacktrace")
				.run();
		}
	}
	
	private static Path downloadAndExtractJRE(BuildConfig config, Path buildDir) throws IOException {
		logger.log(String.format("Downloading and preparing JRE... (version: %S)", config.getJreVersion()));
		
		String os = System.getProperty("os.name").toLowerCase().contains("win") ? "windows" : "linux";
		String jreUrl = "https://api.adoptium.net/v3/binary/latest/%s/ga/%s/x64/jdk/hotspot/normal/adoptium".formatted(config.getJreVersion(), os);
		
		Path tempZip = Files.createTempFile("jre", ".zip");
		Path jreOutputDir = buildDir.resolve("jre/");
		
		if (Files.isDirectory(jreOutputDir)) {
			logger.log(Ansi.green("JRE output directory already exists, skipping download"));
			return jreOutputDir;
		}
		
		// Download JRE
		logger.logf("Downloading JRE from: %s%n(This might take a while)", jreUrl);
		try {
			FileSystem.download(jreUrl, tempZip);
		} catch (URISyntaxException e) {
			logger.error("Failed to download JRE", e);
		}
		
		// Extract JRE
		logger.log("Extracting JRE");
		FileSystem.unzip(tempZip, jreOutputDir);
		
		// Pull contents of JDK directory up and remove JDK directory
		Path jdkDir = Objects.requireNonNull(FileSystem.getSubdirectory(jreOutputDir));
		FileSystem.unwrapDirectory(jdkDir);
		
		return jreOutputDir;
	}
	
	private static Path generateLaunch4jConfig(BuildConfig config, Path projectDir, Path buildDir, Path jrePath, Path launch4jDir) throws IOException {
		logger.log("Generating Launch4j configuration...");
		
		Path jar = projectDir.resolve(config.getJar()).normalize();
		if (!Files.exists(jar)) {
			throw new IllegalStateException(String.format("Missing jar file: %s%nMake sure you have the shadowJar plugin installed.%n", jar));
		}
		
		Path icon = buildDir.resolve(config.getIconPath()).normalize();
		if (!Files.exists(icon)) {
			throw new IllegalStateException(String.format("Missing icon file: %s%n", icon));
		}
		
		String fileName = config.getOutputFileName();
		Path output = buildDir.resolve(fileName).toAbsolutePath();
		
		String copyright = String.format("Copyright (c) %s %s", Year.now(), config.getDeveloper());
		
		String launch4jConfig = """
			<launch4jConfig>
				<dontWrapJar>false</dontWrapJar>
				<chdir>.</chdir>
				<jar>%s</jar>
				<classPath>
					<mainClass>%s</mainClass>
				</classPath>
				<outfile>%s</outfile>
				<jre>
					<minVersion>%s</minVersion>
					<path>%s</path>
					<requiresJdk>false</requiresJdk>
					<requires64Bit>true</requires64Bit>
				</jre>
				<icon>%s</icon>
				<versionInfo>
					<companyName>%s</companyName>
					<productName>%s</productName>
					<fileDescription>%s</fileDescription>
					<fileVersion>%s</fileVersion>
					<txtFileVersion>%s</txtFileVersion>
					<copyright>%s</copyright>
					<productVersion>%s</productVersion>
					<txtProductVersion>%s</txtProductVersion>
					<internalName>%s</internalName>
					<originalFilename>%s</originalFilename>
				</versionInfo>
				<headerType>%s</headerType>
			</launch4jConfig>
			""".formatted(
			 jar,
			config.getMainClass(),
		    output,
			config.getJreVersion(),
			buildDir.relativize(jrePath),
		    icon,
			config.getDeveloper(),
			config.getGameName(),
			config.getGameName(),
			config.getVersion(),
			config.getVersion(),
		    copyright,
			config.getVersion(),
			config.getVersion(),
			config.getGameName(),
			fileName,
			config.isDebug() ? "console" : "gui"
		);
		
		Path launch4jConfigPath = launch4jDir.resolve("config.xml");
		Files.writeString(launch4jConfigPath, launch4jConfig);
		
		Path tempDir = Files.createDirectories(buildDir.resolve("../tmp/pine"));
		Files.writeString(tempDir.resolve("launch4j-config.xml"), launch4jConfig);
		
		return launch4jConfigPath;
	}
	
	private static void bundleResources(BuildConfig config, Path projectDir, Path buildDir) throws IOException {
		logger.log("Bundling resources...");
		
		Path targetDir = Files.createDirectories(buildDir.resolve("resources/"));
		Path resourcesDir = projectDir.resolve(config.getResourcesPath());
		
		FileSystem.copyDirectory(resourcesDir.toFile(), targetDir.toFile());
	}
	
	private static void bundleMods(BuildConfig config, Path buildDir) throws IOException {
		logger.log("Bundling mods...");
		
		Path targetDir = buildDir.resolve("mods/");
		Path modsDir = buildDir.resolve("resources/mods/");
		
		if (Files.exists(targetDir)) {
			FileSystem.deleteDirectory(targetDir);
		}
		
		if (Files.exists(modsDir)) {
			Files.move(modsDir, targetDir);
		} else {
			Files.createDirectories(targetDir);
		}
	}
	
	private static void runLaunch4j(Path configPath, Path launch4jDir) throws IOException, URISyntaxException {
		logger.log("Creating executable with Launch4j...");
		
		URL resource = Objects.requireNonNull(BuildTool.class.getResource("/tools/launch4j"), "Launch4J is missing");
		Path sourceDir = Paths.get(resource.toURI());
		FileSystem.copyDirectory(sourceDir.toFile(), launch4jDir.toFile());
		
		Path launch4jcExecutable = launch4jDir.resolve("launch4jc.exe");
		launch4jcExecutable.toFile().setExecutable(true);
		
		// Run launch4jc.exe with the config file
		ProcessBuilder processBuilder = new ProcessBuilder()
			.command(
			    launch4jcExecutable.toAbsolutePath().toString(),
			    configPath.toString(),
			    DEBUG_LAUNCH4J ? "--l4j-debug-all" : ""
			)
			.directory(launch4jDir.toFile())
			.redirectErrorStream(true);
		
		Process process = processBuilder.start();
		
		// Read Launch4J output
		Logger launch4jLogger = new Logger(new StandardOutputLogHandler(), new StandardErrorLogHandler())
			.setPrefix(Ansi.purple(Logger.formatBadge("Launch4j")));
		if (DEBUG_LAUNCH4J) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					launch4jLogger.log(line.replaceAll("^launch4j: ", ""));
				}
			}
		}
		
		// Check exit code
		try {
			if (process.waitFor() != 0) {
				throw new RuntimeException("Failed to create executable.");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}
	
	private static void finalizeBuild(BuildConfig config, Path buildDir, Path launch4jDir) throws IOException {
		logger.log("Finalizing build...");
		
		FileSystem.deleteDirectory(launch4jDir);
		Files.writeString(buildDir.resolve("version.txt"), config.getVersion());
	}
	
	public static class BuildConfig {
		
		public String mainClass;
		public String gameName;
		public String developer;
		public String version;
		public String jreVersion;
		public String jar;
		public String iconPath;
		public boolean debug = false;
		public String resourcesPath;
		
		public String getMainClass() {
			return Objects.requireNonNullElse(mainClass, "Main");
		}
		
		public String getGameName() {
			return Objects.requireNonNullElse(gameName, "Game");
		}
		
		public String getDeveloper() {
			return Objects.requireNonNullElse(developer, "Unknown");
		}
		
		public String getVersion() {
			return Objects.requireNonNullElse(version, "1.0.0.0");
		}
		
		public String getJreVersion() {
			return Objects.requireNonNullElse(jreVersion, "19");
		}
		
		public String getJar() {
			return Objects.requireNonNullElse(jar, "build/libs/game-1.0-SNAPSHOT-all.jar");
		}
		
		public String getIconPath() {
			return Objects.requireNonNullElse(iconPath, "resources/icon.ico");
		}
		
		public boolean isDebug() {
			return debug;
		}
		
		public String getResourcesPath() {
			return Objects.requireNonNullElse(resourcesPath, "src/main/resources");
		}
	
		public String getOutputFileName() {
			return String.format("%s.exe", getGameName().replaceAll("\\s+", ""));
		}
	}
}

