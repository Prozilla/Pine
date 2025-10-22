package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lwjgl.GLUtils;
import dev.prozilla.pine.common.math.matrix.Matrix2f;
import dev.prozilla.pine.common.math.matrix.Matrix3f;
import dev.prozilla.pine.common.math.matrix.Matrix4f;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

/**
 * Represents an OpenGL shader program.
 */
public class ShaderProgram implements Destructible {
	
	/**
	 * Stores the handle of the program.
	 */
	private final int id;
	
	/**
	 * Creates a shader program.
	 */
	public ShaderProgram() {
		id = glCreateProgram();
	}
	
	/**
	 * Attach a shader to this program.
	 * @param shader Shader to get attached
	 */
	public void attachShader(Shader shader) {
		glAttachShader(id, shader.getId());
	}
	
	/**
	 * Binds the fragment out color variable.
	 * @param number Color number you want to bind
	 * @param name   Variable name
	 */
	public void bindFragmentDataLocation(int number, CharSequence name) {
		glBindFragDataLocation(id, number, name);
	}
	
	/**
	 * Link this program and check its status afterward.
	 */
	public void link() {
		glLinkProgram(id);
		checkStatus();
	}
	
	/**
	 * Sets multiple vertex attributes.
	 * @param names The names of each attribute
	 * @param sizes The amount of floats used for each attribute
	 * @param stride The amount of floats used per vertex
	 */
	public void setVertexAttributes(CharSequence[] names, int[] sizes, int stride) {
		if (names.length != sizes.length) {
			throw new IllegalArgumentException("arrays of names and sizes must have an equal length");
		}
		
		int offset = 0;
		for (int i = 0; i < names.length; i++) {
			setVertexAttribute(names[i], sizes[i], stride, offset);
			offset += sizes[i];
		}
	}
	
	/**
	 * Enables a vertex attribute and sets its pointer.
	 * @param name The name of the attribute
	 * @param size The amount of floats used for the attribute
	 * @param stride The amount of floats used per vertex
	 * @param offset The offset from the first component
	 */
	public void setVertexAttribute(CharSequence name, int size, int stride, int offset) {
		int location = requireAttributeLocation(name);
		enableVertexAttribute(location);
		pointVertexAttribute(location, size, stride, offset);
	}
	
	/**
	 * Enables a vertex attribute.
	 * @param location Location of the vertex attribute
	 */
	public void enableVertexAttribute(int location) {
		glEnableVertexAttribArray(location);
	}
	
	/**
	 * Disables a vertex attribute.
	 * @param location Location of the vertex attribute
	 */
	public void disableVertexAttribute(int location) {
		glDisableVertexAttribArray(location);
	}
	
	/**
	 * Sets the vertex attribute pointer.
	 * @param location Location of the vertex attribute
	 * @param size Number of values per vertex
	 * @param stride Offset between consecutive generic vertex attributes in bytes
	 * @param offset Offset of the first component of the first generic vertex attribute in bytes
	 */
	public void pointVertexAttribute(int location, int size, int stride, int offset) {
		glVertexAttribPointer(location, size, GL_FLOAT, false, stride * Float.BYTES, (long)offset * Float.BYTES);
	}
	
	public int requireAttributeLocation(CharSequence name) throws RuntimeException {
		int location = getAttributeLocation(name);
		if (location == GL_INVALID_INDEX) {
			throw new RuntimeException("shader attribute not found: " + name);
		}
		return location;
	}
	
	/**
	 * Gets the location of an attribute variable with specified name.
	 * @param name Attribute name
	 * @return Location of the attribute
	 */
	public int getAttributeLocation(CharSequence name) {
		return glGetAttribLocation(id, name);
	}
	
	public void setUniform(CharSequence name, int value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, int value) {
		glUniform1i(location, value);
	}
	
	public void setUniform(CharSequence name, float value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, float value) {
		glUniform1f(location, value);
	}
	
	public void setUniform(CharSequence name, Vector2f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Vector2f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(2);
			value.toBuffer(buffer);
			glUniform2fv(location, buffer);
		}
	}
	
	public void setUniform(CharSequence name, Vector3f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Vector3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(3);
			value.toBuffer(buffer);
			glUniform3fv(location, buffer);
		}
	}
	
	public void setUniform(CharSequence name, Vector4f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Vector4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4);
			value.toBuffer(buffer);
			glUniform4fv(location, buffer);
		}
	}
	
	public void setUniform(CharSequence name, Matrix2f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Matrix2f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(2 * 2);
			value.toBuffer(buffer);
			glUniformMatrix2fv(location, false, buffer);
		}
	}
	
	public void setUniform(CharSequence name, Matrix3f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Matrix3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(3 * 3);
			value.toBuffer(buffer);
			glUniformMatrix3fv(location, false, buffer);
		}
	}
	
	public void setUniform(CharSequence name, Matrix4f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
			value.toBuffer(buffer);
			glUniformMatrix4fv(location, false, buffer);
		}
	}
	
	public void setUniform(CharSequence name, int[] value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, int[] value) {
		glUniform1iv(location, value);
	}
	
	public int requireUniformLocation(CharSequence name) throws RuntimeException {
		int location = getUniformLocation(name);
		if (location == GL_INVALID_INDEX) {
			throw new RuntimeException("shader uniform not found: " + name);
		}
		return location;
	}
	
	/**
	 * Gets the location of a uniform variable with specified name.
	 * @param name Uniform name
	 * @return Location of the uniform
	 */
	public int getUniformLocation(CharSequence name) {
		return glGetUniformLocation(id, name);
	}
	
	/**
	 * Use this shader program.
	 */
	public void use() {
		glUseProgram(id);
	}
	
	/**
	 * Checks if the program was linked successfully.
	 */
	public void checkStatus() throws RuntimeException {
		int status = glGetProgrami(id, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetProgramInfoLog(id));
		}
	}
	
	/**
	 * Deletes the shader program.
	 */
	@Override
	public void destroy() {
		glDeleteProgram(id);
	}
	
	public static int getMaxVertexAttributes() {
		return GLUtils.getInt(GL_MAX_VERTEX_ATTRIBS);
	}
	
}
