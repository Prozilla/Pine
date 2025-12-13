package dev.prozilla.pine;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.Application;
import org.lwjgl.Version;

import java.util.StringJoiner;

import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.opengl.GL11.glGetString;

/**
 * Utility class used for querying system/library information.
 */
public final class Pine {
	
	/** Current major version of Pine. */
	public static final int VERSION_MAJOR = 3;
	/** Current minor version of Pine. */
	public static final int VERSION_MINOR = 0;
	/** Current patch version of Pine. */
	public static final int VERSION_PATCH = 1;
	
	private static boolean enableExperimentalFeatures = false;
	
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
	 * @return The name of the platform, or {@code null} if the platform is not supported.
	 */
	public static String getPlatformName() {
		Platform platform = getPlatform();
		if (platform == null) {
			return null;
		}
		return platform.getName();
	}
	
	/**
	 * Returns the platform Pine is running on.
	 * @return The platform, or {@code null} if the platform is not supported.
	 */
	public static Platform getPlatform() {
		return Platform.get();
	}
	
	/**
	 * Returns the architecture Pine is running on.
	 * @return The architecture, or {@code null} if the architecture is not supported.
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
	 * Enables experimental features of Pine.
	 * @see dev.prozilla.pine.common.Experimental
	 */
	public static void enableExperimentalFeatures() {
		enableExperimentalFeatures = true;
		Logger.system.warn("Experimental features are enabled.");
	}
	
	/**
	 * Checks if experimental features are enabled.
	 *
	 * <p>All experimental methods and constructors must call this method to ensure cautious usage of experimental features.</p>
	 * @throws RuntimeException If experimental features are disabled.
	 */
	public static void useExperimentalFeature() throws RuntimeException {
		if (!enableExperimentalFeatures) {
			throw new RuntimeException("experimental features are disabled");
		}
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
		StringJoiner stringJoiner = new StringJoiner("\n");
		
		stringJoiner.add(Logger.formatHeader("Pine Info"));
		stringJoiner.add("Pine version: " + getVersion());
		stringJoiner.add("Platform: " + Platform.getDescriptor());
		stringJoiner.add("Java version: " + getJavaVersion());
		stringJoiner.add("LWJGL version: " + getLWJGLVersion());
		if (Application.isOpenGLInitialized()) {
			stringJoiner.add("OpenGL version: " + getGLVersion());
			stringJoiner.add("OpenGL renderer: " + getGLRenderer());
		}
		stringJoiner.add("GLFW version: " + getGLFWVersion());
		
		logger.log(stringJoiner.toString());
	}
	
}

