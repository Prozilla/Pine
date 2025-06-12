package dev.prozilla.pine;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.Application;
import org.lwjgl.Version;
import org.lwjgl.system.Platform;

import java.util.StringJoiner;

import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.opengl.GL11.glGetString;

/**
 * Utility class used for querying system/library information.
 */
public final class Pine {
	
	/** Current major version of Pine. */
	public static final int VERSION_MAJOR = 2;
	/** Current minor version of Pine. */
	public static final int VERSION_MINOR = 0;
	/** Current patch version of Pine. */
	public static final int VERSION_PATCH = 0;
	
	private Pine() {}
	
	/**
	 * Returns the version of Pine.
	 * @return The Pine version
	 */
	public static String getVersion() {
		return String.format("%s.%s.%s", VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH);
	}
	
	/**
	 * Returns the name of the platform Pine is running on.
	 * @return The name of the platform
	 */
	public static String getPlatformName() {
		return getPlatform().getName();
	}
	
	/**
	 * Returns the platform Pine is running on.
	 * @return The platform
	 */
	public static Platform getPlatform() {
		return Platform.get();
	}
	
	/**
	 * Returns the architecture Pine is running on.
	 * @return The architecture
	 */
	public static Platform.Architecture getArchitecture() {
		return Platform.getArchitecture();
	}
	
	/**
	 * Returns the version of Java.
	 * @return The Java version
	 */
	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}
	
	/**
	 * Returns the version of LWJGL.
	 * @return The LWJGL version
	 */
	public static String getLWJGLVersion() {
		return Version.getVersion();
	}
	
	/**
	 * Returns the version of OpenGL.
	 * @return The OpenGL version
	 * @throws IllegalStateException If OpenGL has not been initialized yet.
	 */
	public static String getGLVersion() throws IllegalStateException {
		Application.requireOpenGL();
		return glGetString(org.lwjgl.opengl.GL11.GL_VERSION);
	}
	
	/**
	 * Returns the renderer of OpenGL.
	 * @return The renderer of OpenGL
	 * @throws IllegalStateException If OpenGL has not been initialized yet.
	 */
	public static String getGLRenderer() {
		Application.requireOpenGL();
		return glGetString(org.lwjgl.opengl.GL11.GL_RENDERER);
	}
	
	/**
	 * Returns the version of GLFW.
	 * @return The GLFW version
	 */
	public static String getGLFWVersion() {
		return glfwGetVersionString();
	}
	
	/**
	 * Prints all system and library information to the system logger.
	 */
	public static void print() {
		print(Logger.system);
	}
	
	/**
	 * Prints all system and library information.
	 */
	public static void print(Logger logger) {
		StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());
		
		stringJoiner.add(Logger.formatHeader("Pine Info"));
		stringJoiner.add("Pine version: " + getVersion());
		stringJoiner.add("Platform: " + getPlatformName());
		stringJoiner.add("Architecture: " + getArchitecture());
		stringJoiner.add("Java version: " + getJavaVersion());
		stringJoiner.add("LWJGL version: " + getLWJGLVersion());
		if (Application.initializedOpenGL) {
			stringJoiner.add("OpenGL version: " + getGLVersion());
			stringJoiner.add("OpenGL renderer: " + getGLRenderer());
		}
		stringJoiner.add("GLFW version: " + getGLFWVersion());
		
		logger.log(stringJoiner.toString());
	}
	
}

