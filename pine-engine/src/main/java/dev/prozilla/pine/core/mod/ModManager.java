package dev.prozilla.pine.core.mod;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.state.input.Input;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static dev.prozilla.pine.common.system.PathUtils.normalizePath;

/**
 * Class responsible for loading and keeping track of modifications (mods).
 */
public class ModManager implements Initializable, Destructible {
	
	private final List<ModEntry> mods;
	
	private final Application application;
	private final Logger logger;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public ModManager(Application application) {
		this.application = application;
		logger = application.getLogger();
		
		mods = new ArrayList<>();
	}
	
	/**
	 * Loads all mods from the <code>mods</code> directory by looking for jar files.
	 */
	@Override
	public void init() {
		String modsPath = null;
		
		// Look for mods directory (production build)
		File modsDirectory = new File("mods");
		if (modsDirectory.exists()) {
			modsPath = modsDirectory.getAbsolutePath();
		}
		
		// Alternatively, look for mods directory inside resources (development build)
		if (modsPath == null) {
			URL modsUrl = ModManager.class.getResource("/mods");
			if (modsUrl != null) {
				modsPath = normalizePath(modsUrl.getPath());
			}
		}
		
		if (modsPath == null) {
			logger.log("No mods directory found.");
			return;
		}
		
		// Check if mods directory exists
		File modsFolder = new File(modsPath);
		if (!modsFolder.exists() || !modsFolder.isDirectory()) {
			return;
		}
		
		logger.logPath("Loading mods from", modsPath);
		
		// Get all jar files
		File[] files = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
		if (files == null || files.length == 0) {
			logger.log("No mods found");
			return;
		}
		
		logger.logf("Found %s mod(s)", files.length);
		
		for (File file : files) {
			loadMod(file);
		}
	}
	
	/**
	 * Loads a mod and its metadata based on the path of a jar file.
	 * @param jarFile Jar file of the mod
	 */
	private void loadMod(File jarFile) {
		try {
			// Load mod class from jar file
			URLClassLoader loader = new URLClassLoader(new URL[]{ jarFile.toURI().toURL() });
			ServiceLoader<Mod> serviceLoader = ServiceLoader.load(Mod.class, loader);
			
			// Read and parse mod metadata
			ModMetadata metadata = null;
			InputStream metadataStream = loader.getResourceAsStream("mod.json");
			if (metadataStream != null) {
				metadata = objectMapper.readValue(metadataStream, ModMetadata.class);
			}
			
			// Initialize mod
			for (Mod mod : serviceLoader) {
				ModEntry modEntry = new ModEntry(mod, metadata);
				logger.log("Loading mod: " + modEntry);
				mod.init(application);
				mods.add(modEntry);
			}
		} catch (Exception e) {
			logger.error("Failed to load mod: " + jarFile.getAbsolutePath(), e);
		}
	}
	
	public void beforeInput(float deltaTime) {
		if (isEmpty()) {
			return;
		}
		
		Input input = application.getInput();
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeInput(input, deltaTime);
		}
	}
	
	public void afterInput(float deltaTime) {
		if (isEmpty()) {
			return;
		}
		
		Input input = application.getInput();
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterInput(input, deltaTime);
		}
	}
	
	public void beforeUpdate(float deltaTime) {
		if (isEmpty()) {
			return;
		}
		
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeUpdate(deltaTime);
		}
	}
	
	public void afterUpdate(float deltaTime) {
		if (isEmpty()) {
			return;
		}
		
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterUpdate(deltaTime);
		}
	}
	
	public void beforeRender() {
		if (isEmpty()) {
			return;
		}
		
		Renderer renderer = application.getRenderer();
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeRender(renderer);
		}
	}
	
	public void afterRender() {
		if (isEmpty()) {
			return;
		}
		
		Renderer renderer = application.getRenderer();
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterRender(renderer);
		}
	}
	
	/**
	 * Removes all mods.
	 */
	@Override
	public void destroy() {
		Destructible.destroyAll(mods);
	}
	
	/**
	 * Returns the metadata of all active mods.
	 */
	public ModMetadata[] getMods() {
		int modCount = mods.size();
		ModMetadata[] modsMetadata = new ModMetadata[modCount];
		
		for (int i = 0; i < modCount; i++) {
			modsMetadata[i] = mods.get(i).metadata;
		}
		
		return modsMetadata;
	}
	
	/**
	 * Returns true if there are no mods active.
	 */
	public boolean isEmpty() {
		return mods.isEmpty();
	}
}
