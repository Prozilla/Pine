package dev.prozilla.pine.core.rendering;

import dev.prozilla.pine.common.IntEnum;
import dev.prozilla.pine.common.asset.Asset;
import dev.prozilla.pine.common.util.EnumUtils;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

/**
 * Represents an OpenGL shader.
 */
public class Shader implements Asset {
	
	/**
	 * Stores the handle of the shader.
	 */
	private final int id;
	
	private String path;
	
	public Shader(Type type) {
		this(type.getValue());
	}
	
	/**
	 * Creates a shader with specified type.
	 * @param type Type of the shader
	 */
	public Shader(int type) {
		id = glCreateShader(type);
	}
	
	/**
	 * Sets the source code of this shader.
	 * @param source GLSL Source Code for the shader
	 */
	public void setSource(CharSequence source) {
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
	 * Creates a shader with specified type and source and compiles it.
	 * @param type Type of the shader
	 * @param source Source of the shader
	 * @return Compiled Shader from the specified source
	 */
	public static Shader createShader(Type type, CharSequence source) {
		return createShader(type.getValue(), source);
	}
	
	/**
	 * Creates a shader with specified type and source and compiles it.
	 * @param type Type of the shader
	 * @param source Source of the shader
	 * @return Compiled Shader from the specified source
	 */
	public static Shader createShader(int type, CharSequence source) {
		Shader shader = new Shader(type);
		shader.setSource(source);
		shader.compile();
		
		return shader;
	}
	
	public enum Type implements IntEnum {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		COMPUTE( GL_COMPUTE_SHADER),
		TESS_CONTROL(GL_TESS_CONTROL_SHADER),
		TESS_EVALUATION(GL_TESS_EVALUATION_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER);
		
		private final int value;
		
		Type(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static boolean isValid(int value) {
			return EnumUtils.hasIntValue(values(), value);
		}
		
	}
	
}
