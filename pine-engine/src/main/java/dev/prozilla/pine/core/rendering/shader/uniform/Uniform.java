package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.MutableProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

/**
 * A uniform associated with a shader program that passes data to the shaders on the GPU.
 * @param <T> The type of this uniform
 */
public interface Uniform<T> extends MutableProperty<T> {
	
	/**
	 * Checks if this uniform has a valid location.
	 * @return {@code true} if the location of this uniform is valid.
	 */
	default boolean isValid() {
		return getLocation() != GL_INVALID_INDEX;
	}
	
	/**
	 * Returns the location of this uniform.
	 * @return The location of this uniform.
	 */
	int getLocation();
	
	/**
	 * Returns the shader program associated with this uniform.
	 * @return The shader program associated with this uniform.
	 */
	ShaderProgram getProgram();
	
}
