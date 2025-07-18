package dev.prozilla.pine.common.system;

import dev.prozilla.pine.common.util.checks.Checks;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for handling path strings.
 */
public final class PathUtils {
	
	private PathUtils() {}
	
	/**
	 * Removes a leading slash from a path if there is one.
	 * @param path Path with or without leading slash
	 * @return Path without leading slash
	 */
	public static String removeLeadingSlash(String path) {
		Checks.isNotNull(path, "path");
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
		Checks.isNotNull(path, "path");
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
		Checks.isNotNull(path, "path");
		
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
	
	/**
	 * Returns a path relative to the source root.
	 * @param path The original path
	 * @return The path relative to the source root
	 */
	public static String relativizePath(String path) {
		Checks.isNotNull(path, "path");
		
		Path currentDir = Paths.get("").toAbsolutePath();
		Path absolute = Paths.get(path).toAbsolutePath();
		Path relative = currentDir.relativize(absolute);
		
		return relative.toString();
	}
	
	/**
	 * Creates a clickable filepath URL.
	 * @param path The path of the file or folder
	 * @return A link to the file or folder
	 */
	public static String createLink(String path) {
		Checks.isNotNull(path, "path");
		Path absolute = Paths.get(path).toAbsolutePath();
		return "file:///" + absolute.toString().replace("\\", "/").replace(" ", "%20");
	}
	
	/**
	 * Returns the file extension of a given path.
	 * @param path The path of a file
	 * @return The file extension, or an empty string if {@code path} is {@code null} or there is no file extension.
	 */
	public static String getFileExtension(String path) {
		if (path == null) {
			return "";
		}
		int lastIndexOf = path.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return path.substring(lastIndexOf + 1).toLowerCase();
	}
	
}
