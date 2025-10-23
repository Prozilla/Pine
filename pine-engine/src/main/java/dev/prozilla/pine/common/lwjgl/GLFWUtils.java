package dev.prozilla.pine.common.lwjgl;

import org.jetbrains.annotations.Contract;
import org.lwjgl.system.NativeResource;

/**
 * GLFW utilities.
 */
public final class GLFWUtils {
	
	private GLFWUtils() {}
	
	public static void free(NativeResource... nativeResources) {
		for (NativeResource nativeResource : nativeResources) {
			free(nativeResource);
		}
	}
	
	@Contract("_ -> null")
	public static <N extends NativeResource> N free(N nativeResource) {
		if (nativeResource != null) {
			nativeResource.free();
		}
		return null;
	}
	
}
