package dev.prozilla.pine.core.mod;

import dev.prozilla.pine.common.Lifecycle;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static dev.prozilla.pine.common.system.Path.normalizePath;

public class ModManager implements Lifecycle {
	
	private final List<Mod> mods;
	
	public ModManager() {
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
		
		// Look for mods directory inside resources (development build)
		URL modsUrl = ModManager.class.getResource("/mods");
		if (modsUrl != null) {
			modsPath = normalizePath(modsUrl.getPath());
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
			try {
				// Load mod class from jar file
				URLClassLoader loader = new URLClassLoader(new URL[]{ file.toURI().toURL() });
				ServiceLoader<Mod> serviceLoader = ServiceLoader.load(Mod.class, loader);
				
				// Initialize mod
				for (Mod mod : serviceLoader) {
					System.out.println("Loading mod: " + mod.getClass().getName());
					mod.init();
					mods.add(mod);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to load mod: " + file.getAbsolutePath());
			}
		}
	}
	
	@Override
	public void input(float deltaTime) {
		for (Mod mod : mods) {
			mod.input(deltaTime);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		for (Mod mod : mods) {
			mod.update(deltaTime);
		}
	}
	
	@Override
	public void render() {
		for (Mod mod : mods) {
			mod.render();
		}
	}
	
	@Override
	public void destroy() {
		for (Mod mod : mods) {
			mod.destroy();
		}
		
		mods.clear();
	}
}
