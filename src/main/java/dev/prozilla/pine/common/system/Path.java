package dev.prozilla.pine.common.system;

import java.nio.file.FileSystems;

/**
 * Utility class for handling path strings.
 */
public class Path {
	/**
	 * Removes a leading slash from a path if there is one.
	 * @param path Path with or without leading slash
	 * @return Path without leading slash
	 */
	public static String removeLeadingSlash(String path) {
		if (path.startsWith("/")) {
			return path.substring(1);
		} else {
			return path;
		}
	}
	
	/**
	 * Normalizes a path by removing prefixes and replacing separators.
	 * @param path Path
	 * @return Normalized path
	 */
	public static String normalizePath(String path) {
		// Handle file:/ prefix
		if (path.startsWith("file:/")) {
			path = path.substring("file:/".length());
		}
		
		// Remove leading slash for Windows paths
		if (path.charAt(2) == ':') {
			path = Path.removeLeadingSlash(path);
		}
		
		// Replace path separator
		path = path.replace("/", FileSystems.getDefault().getSeparator());
		
		return path;
	}
}
