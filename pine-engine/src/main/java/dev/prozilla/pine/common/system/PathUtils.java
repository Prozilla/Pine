package dev.prozilla.pine.common.system;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

/**
 * Utility class for handling path strings.
 */
public final class PathUtils {
	
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
	 * Adds a leading slash to a path if there isn't one.
	 * @param path Path with or without leading slash
	 * @return Path with leading slash
	 */
	public static String addLeadingSlash(String path) {
		if (path.startsWith("/")) {
			return path;
		} else {
			return "/" + path;
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
			path = PathUtils.removeLeadingSlash(path);
		}
		
		// Replace path separator
		path = path.replace("/", FileSystems.getDefault().getSeparator());
		
		return path;
	}
	
	public static String relativizePath(String path) {
		java.nio.file.Path currentDir = Paths.get("").toAbsolutePath();
		java.nio.file.Path absolute = Paths.get(path).toAbsolutePath();
		java.nio.file.Path relative = currentDir.relativize(absolute);
		
		return relative.toString();
	}
	
	public static String createLink(String path) {
		java.nio.file.Path absolute = Paths.get(path).toAbsolutePath();
		return "file:///" + absolute.toString().replace("\\", "/");
	}
	
}
