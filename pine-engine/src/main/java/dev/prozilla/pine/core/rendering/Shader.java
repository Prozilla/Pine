package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.asset.Asset;

import static org.lwjgl.opengl.GL20.*;

/**
 * Represents an OpenGL shader.
 */
public class Shader implements Asset {
	
	/**
	 * Stores the handle of the shader.
	 */
	private final int id;
	
	private String path;
	
	/**
	 * Creates a shader with specified type. The type in the tutorial should be
	 * either <code>GL_VERTEX_SHADER</code> or <code>GL_FRAGMENT_SHADER</code>.
	 * @param type Type of the shader
	 */
	public Shader(int type) {
		id = glCreateShader(type);
	}
	
	/**
	 * Sets the source code of this shader.
	 * @param source GLSL Source Code for the shader
	 */
	public void source(CharSequence source) {
		glShaderSource(id, source);
	}
	
	/**
	 * Compiles the shader and checks its status afterward.
	 */
	public void compile() {
		glCompileShader(id);
		
		checkStatus();
	}
	
	/**
	 * Checks if the shader was compiled successfully.
	 */
	private void checkStatus() throws RuntimeException {
		int status = glGetShaderi(id, GL_COMPILE_STATUS);
		if (status != GL_TRUE) {
			throw new RuntimeException(glGetShaderInfoLog(id));
		}
	}
	
	/**
	 * Deletes the shader.
	 */
	@Override
	public void destroy() {
		glDeleteShader(id);
	}
	
	/**
	 * Getter for the shader ID.
	 * @return Handle of this shader
	 */
	public int getId() {
		return id;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Creates a shader with specified type and source and compiles it. The type
	 * in the tutorial should be either <code>GL_VERTEX_SHADER</code> or
	 * <code>GL_FRAGMENT_SHADER</code>.
	 * @param type   Type of the shader
	 * @param source Source of the shader
	 * @return Compiled Shader from the specified source
	 */
	public static Shader createShader(int type, CharSequence source) {
		Shader shader = new Shader(type);
		shader.source(source);
		shader.compile();
		
		return shader;
	}
	
}
