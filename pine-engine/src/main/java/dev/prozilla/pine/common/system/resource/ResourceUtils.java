package dev.prozilla.pine.common.system.resource;

import dev.prozilla.pine.common.system.PathUtils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static dev.prozilla.pine.common.system.PathUtils.normalizePath;

/**
 * Utility class for handling resources.
 */
public class ResourceUtils {
	
	/**
	 * Finds the resource and returns its normalized path.
	 * @param path Path to the resource relative to the resources directory
	 * @return Normalized path to the resource
	 */
	public static String getResourcePath(String path) throws RuntimeException {
		path = PathUtils.removeLeadingSlash(path);
		
		// Attempt to load resource as file
		File resourceFile = new File("resources/" + path);
		if (resourceFile.exists()) {
			return resourceFile.getAbsolutePath();
		}
		
		// Use resource loader
		URL resource = ResourceUtils.class.getResource("/" + path);
		
		if (resource == null) {
			throw new RuntimeException("Resource not found: " + path);
		}
		
		return normalizePath(URLDecoder.decode(resource.getPath(), StandardCharsets.UTF_8));
	}
}
