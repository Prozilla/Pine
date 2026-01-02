package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.prozilla.pine.common.system.PathUtils.normalizePath;

/**
 * Utility class for handling resources.
 */
public final class ResourceUtils {
	
	private ResourceUtils() {}
	
	public static InputStream createResourceFileInputStream(String path) {
		return createResourceFileInputStream(path, getResourceFilePath(path));
	}
	
	public static InputStream createResourceFileInputStream(String path, Path filePath) {
		if (filePath == null) {
			return null;
		}
		
		try {
			return new FileInputStream(filePath.toFile());
		} catch (FileNotFoundException e) {
			Logger.system.error("File not found: " + path, e);
		}
		
		return null;
	}
	
	public static Path getResourceFilePath(String path) {
		if (Application.isDevMode()) {
			File originalFile = new File("src/main/resources/" + PathUtils.removeLeadingSlash(path));
			if (originalFile.exists()) {
				return Path.of(originalFile.getAbsolutePath());
			}
		}
		
		try {
			return Paths.get(ResourceUtils.getResourcePath(path)).toAbsolutePath();
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * Finds the resource and returns its normalized path.
	 *
	 * <p>Searches in the "resources/" folder first, then in the classpath.</p>
	 * @param path Path to the resource relative to the resources directory
	 * @return Normalized path to the resource
	 * @throws RuntimeException If the resource was not found
	 */
	public static String getResourcePath(String path) throws RuntimeException {
		path = PathUtils.removeLeadingSlash(path);
		
		// Try loading from "resources/" folder on disk
		File resourceFile = new File("resources/" + path);
		if (resourceFile.exists()) {
			return resourceFile.getAbsolutePath();
		}
		
		// Try loading from classpath
		URL resource = ResourceUtils.class.getResource("/" + path);
		if (resource == null) {
			throw new RuntimeException("Resource not found: " + path);
		}
		return normalizePath(URLDecoder.decode(resource.getPath(), StandardCharsets.UTF_8));
	}
	
	/**
	 * Finds the resource and returns it as an InputStream.
	 *
	 * <p>Searches in the "resources/" folder first, then in the classpath.</p>
	 * @param path Path to the resource relative to the resources directory
	 * @return InputStream of the resource
	 * @throws RuntimeException If the resource was not found or cannot be opened
	 */
	public static InputStream getResourceStream(String path) throws RuntimeException {
		path = PathUtils.removeLeadingSlash(path);
		
		// Try loading from "resources/" folder on disk
		File resourceFile = new File("resources/" + path);
		if (resourceFile.exists()) {
			try {
				return new FileInputStream(resourceFile);
			} catch (Exception e) {
				throw new RuntimeException("Failed to open resource file: " + path, e);
			}
		}
		
		// Try loading from classpath
		InputStream stream = ResourceUtils.class.getResourceAsStream("/" + path);
		if (stream == null) {
			throw new RuntimeException("Resource not found: " + path);
		}
		return stream;
	}
	
}
