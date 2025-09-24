package dev.prozilla.pine.common.lwjgl;

import org.lwjgl.system.NativeResource;

/**
 * GLFW utilities.
 */
public final class GLFWUtils {
	
	private GLFWUtils() {}
	
	public static void freeNativeResources(NativeResource... nativeResources) {
		for (NativeResource nativeResource : nativeResources) {
			if (nativeResource != null) {
				nativeResource.free();
			}
		}
	}
	
}
