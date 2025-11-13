package dev.prozilla.pine.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.logging.handler.StandardErrorLogHandler;
import dev.prozilla.pine.common.logging.handler.StandardOutputLogHandler;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.common.system.FileSystem;
import dev.prozilla.pine.common.system.Platform;
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
	/** Output directory for the build tool. */
	public static final String BUILD_PATH = "build/pine";
	/** Temporary directory for the build tool. */
	public static final String TEMP_PATH = "build/tmp/pine";
	public static final String LAUNCH4J_TEMP_PATH = "launch4j";
	public static final String NSIS_TEMP_PATH = "nsis";
	public static final String CONFIG_NAME = "pine-config.json";
	
	// Debugging
	private static final boolean DEBUG_LAUNCH4J = true;
	private static final boolean DEBUG_NSIS = true;
	private static final boolean FINALIZE_ONLY = false;
	
	private static final Logger logger = new Logger(new StandardOutputLogHandler(), new StandardOutputLogHandler())
		.setPrefix(Logger.formatBadge("build", Ansi.GREEN));
	
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
		if (!FINALIZE_ONLY) {
			prepareBuildDir(buildDir);
		}
		Path launch4jDir = Files.createDirectories(buildDir.resolve(LAUNCH4J_TEMP_PATH));
		Path tempDir = Files.createDirectories(projectDir.resolve(TEMP_PATH));

		if (!FINALIZE_ONLY) {
			buildShadowJar(projectDir);
			Path jrePath = downloadAndExtractJRE(config, buildDir);
			bundleResources(config, projectDir, buildDir);
			bundleMods(buildDir);
			
			Path launch4jConfigPath = generateLaunch4jConfig(config, projectDir, buildDir, jrePath, launch4jDir, tempDir);
			runLaunch4j(launch4jConfigPath, launch4jDir);
		} else {
			logger.log("Skipping main build steps");
		}
		
		finalizeBuild(config, buildDir, launch4jDir, tempDir);
		logger.logPath(Ansi.green("Build completed in"), buildDir.toAbsolutePath().toString());
	}
	
	private static void prepareBuildDir(Path buildDir) {
		logger.log("Clearing build directory...");
		for (File file : Objects.requireNonNull(buildDir.toFile().listFiles(), "build directory is not a directory")) {
			if (!file.isDirectory() || !file.getName().equals("jre")) {
				file.delete();
			}
		}
	}
	
	private static void buildShadowJar(Path projectDir) {
		logger.log("Building shadow jar...");
		
		GradleConnector connector = GradleConnector.newConnector()
	        .useGradleUserHomeDir(new File(System.getProperty("user.home"), ".gradle"))
	        .forProjectDirectory(projectDir.toFile());
		
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
		
		String os = Platform.get().getIdentifier();
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
		Path jdkDir = Objects.requireNonNull(FileSystem.getSubdirectory(jreOutputDir), "JRE output directory not found");
		FileSystem.unwrapDirectory(jdkDir);
		
		return jreOutputDir;
	}
	
	private static Path generateLaunch4jConfig(BuildConfig config, Path projectDir, Path buildDir, Path jrePath, Path launch4jDir, Path tempDir) throws IOException {
		logger.log("Generating Launch4j configuration...");
		
		Path jar = projectDir.resolve(config.getJar()).normalize();
		if (!Files.exists(jar)) {
			throw new IllegalStateException(String.format("Missing jar file: %s%nMake sure you have the shadowJar plugin installed.%n", jar));
		}
		
		Path icon = buildDir.resolve(config.getIconPath()).normalize();
		if (!Files.exists(icon)) {
			logger.logPath("File not found", icon.toString());
			icon = null;
		}
		
		String fileName = config.getOutputFileName();
		Path output = buildDir.resolve(fileName).toAbsolutePath();
		
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
				%s
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
			icon != null ? String.format("<icon>%s</icon>", icon) : "",
			config.getDeveloper(),
			config.getGameName(),
			config.getGameName(),
			config.getVersion(),
			config.getVersion(),
		    config.getCopyright(),
			config.getVersion(),
			config.getVersion(),
			config.getGameName(),
			fileName,
			config.isDebug() ? "console" : "gui"
		);
		
		Path launch4jConfigPath = launch4jDir.resolve("config.xml");
		Files.writeString(launch4jConfigPath, launch4jConfig);
		Files.writeString(tempDir.resolve("launch4j-config.xml"), launch4jConfig);
		
		return launch4jConfigPath;
	}
	
	private static void bundleResources(BuildConfig config, Path projectDir, Path buildDir) throws IOException {
		logger.log("Bundling resources...");
		
		Path targetDir = Files.createDirectories(buildDir.resolve("resources/"));
		Path resourcesDir = projectDir.resolve(config.getResourcesPath());
		
		FileSystem.copyDirectory(resourcesDir.toFile(), targetDir.toFile());
	}
	
	private static void bundleMods(Path buildDir) throws IOException {
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
			.setPrefix(Logger.formatBadge("Launch4j", Ansi.PURPLE));
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
	
	private static void finalizeBuild(BuildConfig config, Path buildDir, Path launch4jDir, Path tempDir) throws IOException, URISyntaxException {
		logger.log("Finalizing build...");
		
		FileSystem.deleteDirectory(launch4jDir);
		Files.writeString(buildDir.resolve("version.txt"), config.getVersion());
		
		if (config.shouldIncludeZip() && (!FINALIZE_ONLY || !Files.exists(buildDir.resolve(config.getZipFileName())))) {
			logger.log("Creating ZIP...");
			FileSystem.zip(buildDir, config.getZipFileName());
		}
		
		if (config.shouldIncludeInstaller()) {
			Path nsisDir = Files.createDirectories(buildDir.resolve(NSIS_TEMP_PATH));
			Path nsisScriptPath = generateNSISScript(config, buildDir, nsisDir, tempDir);
			runNSIS(nsisScriptPath, nsisDir);
			FileSystem.deleteDirectory(nsisDir);
		}
	}
	
	private static Path generateNSISScript(BuildConfig config, Path buildDir, Path nsisDir, Path tempDir) throws IOException {
		logger.log("Generating NSIS configuration...");
		
		String nsisScript = """
		!include MUI2.nsh
		
		!define APP_NAME "%s"
		!define APP_VERSION "%s"
		!define ZIP_FILE "%s"
		!define EXE_FILE "%s"
		!define PUBLISHER "%s"
		!define ICON "%s"

		;--------------------------------
		; Compilation arguments
		!define INSTALLER_ZIP_FILE "%s"
		!define INSTALLER_ICON "%s"
		!define INSTALLER_WORKING_DIR "%s"
		!define INSTALLER_OUTFILE "%s"
		
		;--------------------------------
		; Installer info
		
		OutFile "%s"
		InstallDir "$PROGRAMFILES\\${APP_NAME}"
		Name "${APP_NAME}"
		RequestExecutionLevel admin
		ManifestDPIAware true
		
		;--------------------------------
		; MUI configuration
		
		!define MUI_ICON "${INSTALLER_ICON}"
		!define MUI_UNICON "${INSTALLER_ICON}"
		
		;--------------------------------
		; Pages
		
		!insertmacro MUI_PAGE_WELCOME
		!insertmacro MUI_PAGE_COMPONENTS
		!insertmacro MUI_PAGE_DIRECTORY
		!insertmacro MUI_PAGE_INSTFILES
		!insertmacro MUI_PAGE_FINISH
		!insertmacro MUI_UNPAGE_CONFIRM
		!insertmacro MUI_UNPAGE_INSTFILES
		!insertmacro MUI_LANGUAGE "English"
		
		;--------------------------------
		; Sections

		Section "Install" SecMain
			SectionIn RO
			SetOutPath "$INSTDIR"
		
			File /r /x "${INSTALLER_WORKING_DIR}" /x "${INSTALLER_OUTFILE}" /x "${ZIP_FILE}" "..\\*.*"
		
			WriteUninstaller "$INSTDIR\\Uninstall.exe"
		
			WriteRegStr HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}" "DisplayName" "${APP_NAME}"
			WriteRegStr HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}" "UninstallString" "$INSTDIR\\Uninstall.exe"
			WriteRegStr HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}" "DisplayVersion" "${APP_VERSION}"
			WriteRegStr HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}" "Publisher" "${PUBLISHER}"
			WriteRegStr HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}" "DisplayIcon" "$INSTDIR\\${ICON}"
		SectionEnd
		
		Section "Start Menu Shortcut" SecStartMenu
			CreateDirectory "$SMPROGRAMS\\${APP_NAME}"
			CreateShortCut "$SMPROGRAMS\\${APP_NAME}\\${APP_NAME}.lnk" "$INSTDIR\\${EXE_FILE}"
			CreateShortCut "$SMPROGRAMS\\${APP_NAME}\\Uninstall ${APP_NAME}.lnk" "$INSTDIR\\Uninstall.exe"
		SectionEnd
		
		Section "Desktop Shortcut" SecDesktop
			CreateShortCut "$DESKTOP\\${APP_NAME}.lnk" "$INSTDIR\\${EXE_FILE}"
		SectionEnd
		
		Section "Uninstall"
			Delete "$DESKTOP\\${APP_NAME}.lnk"
			RMDir /r "$INSTDIR"
			RMDir /r "$SMPROGRAMS\\${APP_NAME}"
			DeleteRegKey HKLM "Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\${APP_NAME}"
		SectionEnd
		
		;--------------------------------
		; Component descriptions
		
		!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
		    !insertmacro MUI_DESCRIPTION_TEXT ${SecStartMenu} "Create a Start Menu shortcut"
		    !insertmacro MUI_DESCRIPTION_TEXT ${SecDesktop} "Create a Desktop shortcut"
		!insertmacro MUI_FUNCTION_DESCRIPTION_END
		""".formatted(
			config.getGameName(),
			config.getVersion(),
			Path.of(config.getZipFileName()),
			config.getOutputFileName(),
			config.getDeveloper(),
			Path.of(config.getIconPath()),
			nsisDir.relativize(buildDir.resolve(config.getZipFileName())),
			nsisDir.relativize(buildDir.resolve(config.getIconPath())),
			NSIS_TEMP_PATH,
			config.getInstallerName(),
			nsisDir.relativize(buildDir.resolve(config.getInstallerName()))
		);
		
		Path nsisScriptPath = nsisDir.resolve("installer.nsi");
		Files.writeString(nsisScriptPath, nsisScript);
		Files.writeString(tempDir.resolve("nsis-installer.nsi"), nsisScript);
		
		return nsisScriptPath;
	}
	
	private static void runNSIS(Path scriptPath, Path nsisDir) throws URISyntaxException, IOException {
		logger.log("Creating installer with NSIS...");
		
		URL resource = Objects.requireNonNull(BuildTool.class.getResource("/tools/nsis"), "NSIS is missing");
		Path sourceDir = Paths.get(resource.toURI());
		FileSystem.copyDirectory(sourceDir.toFile(), nsisDir.toFile());
		
		Path nsisExecutable = nsisDir.resolve("makensis.exe");
		nsisExecutable.toFile().setExecutable(true);
		
		// TODO: make abstract
		
		// Run makensis.exe with the config file
		ProcessBuilder processBuilder = new ProcessBuilder()
			.command(
				nsisExecutable.toAbsolutePath().toString(),
				scriptPath.toAbsolutePath().toString()
			)
			.directory(nsisDir.toFile())
			.redirectErrorStream(true);
		
		Process process = processBuilder.start();
		
		// Read NSIS output
		Logger nsisLogger = new Logger(new StandardOutputLogHandler(), new StandardErrorLogHandler())
			.setPrefix(Logger.formatBadge("NSIS", Ansi.PURPLE));
		
		if (DEBUG_NSIS) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					nsisLogger.log(line.replaceAll("^nsis: ", ""));
				}
			}
		}
		
		// Check exit code
		try {
			if (process.waitFor() != 0) {
				throw new RuntimeException("Failed to create installer.");
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
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
		public boolean includeZip = true;
		public boolean includeInstaller = true;
		
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
	
		public boolean shouldIncludeZip() {
			return includeZip || shouldIncludeInstaller();
		}
		
		public String getOutputFileName() {
			return getFileName("exe");
		}
		
		public String getZipFileName() {
			return getFileName("zip");
		}
		
		public String getInstallerName() {
			return getFileName("-installer", "exe");
		}
		
		public String getFileName(String extension) {
			return getFileName("", extension);
		}
		
		public String getFileName(String suffix, String extension) {
			return String.format("%s%s.%s", getGameName().replaceAll("\\s+", ""), suffix, extension);
		}
		
		public boolean shouldIncludeInstaller() {
			return includeInstaller && Platform.get() == Platform.WINDOWS;
		}
		
		public String getCopyright() {
			return String.format("Copyright (c) %s %s", Year.now(), getDeveloper());
		}
		
	}
}

