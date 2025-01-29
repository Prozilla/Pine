package dev.prozilla.pine.core.mod;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.prozilla.pine.common.Lifecycle;
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

import static dev.prozilla.pine.common.system.Path.normalizePath;

public class ModManager implements Lifecycle {
	
	private final List<ModEntry> mods;
	
	private final Application application;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public ModManager(Application application) {
		this.application = application;
		
		mods = new ArrayList<>();
	}
	
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
			System.out.println("No mods directory found.");
			return;
		}
		
		// Check if mods directory exists
		File modsFolder = new File(modsPath);
		if (!modsFolder.exists() || !modsFolder.isDirectory()) {
			return;
		}
		
		System.out.println("Loading mods from: " + modsPath);
		
		// Get all jar files
		File[] files = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
		if (files == null || files.length == 0) {
			System.out.println("No mods found");
			return;
		}
		
		System.out.printf("Found %s mod(s)%n", files.length);
		
		for (File file : files) {
			loadMod(file);
		}
	}
	
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
				System.out.println("Loading mod: " + modEntry);
				mod.init(application);
				mods.add(modEntry);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to load mod: " + jarFile.getAbsolutePath());
		}
	}
	
	public void beforeInput(float deltaTime) {
		Input input = application.getInput();
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeInput(input, deltaTime);
		}
	}
	
	public void afterInput(float deltaTime) {
		Input input = application.getInput();
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterInput(input, deltaTime);
		}
	}
	
	public void beforeUpdate(float deltaTime) {
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeUpdate(deltaTime);
		}
	}
	
	public void afterUpdate(float deltaTime) {
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterUpdate(deltaTime);
		}
	}
	
	public void beforeRender() {
		Renderer renderer = application.getRenderer();
		for (ModEntry modEntry : mods) {
			modEntry.mod.beforeRender(renderer);
		}
	}
	
	public void afterRender() {
		Renderer renderer = application.getRenderer();
		for (ModEntry modEntry : mods) {
			modEntry.mod.afterRender(renderer);
		}
	}
	
	@Override
	public void destroy() {
		for (ModEntry modEntry : mods) {
			modEntry.mod.destroy();
		}
		
		mods.clear();
	}
}
