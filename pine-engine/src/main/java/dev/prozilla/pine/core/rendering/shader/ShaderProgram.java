package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.asset.pool.AssetPoolEvent;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.exception.GLException;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.lifecycle.Initializable;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.lwjgl.GLUtils;
import dev.prozilla.pine.common.math.matrix.Matrix2f;
import dev.prozilla.pine.common.math.matrix.Matrix3f;
import dev.prozilla.pine.common.math.matrix.Matrix4f;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Vertex;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;
import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

/**
 * Represents an OpenGL shader program.
 */
public abstract class ShaderProgram implements Destructible, Initializable {
	
	/** Stores the handle of the program. */
	private final int id;
	private boolean isInitialized;
	protected Logger logger;
	
	private static UnlitShaderProgram unlitShaderProgram;
	private static LitShaderProgram litShaderProgram;
	private static DepthShaderProgram depthShaderProgram;
	
	// Uniforms
	public static final String PROJECTION_UNIFORM = "uProjection";
	public static final String VIEW_UNIFORM = "uView";
	public static final String MODEL_UNIFORM = "uModel";
	
	public static final String DEFAULT_OUTPUT_VARIABLE = "color";
	
	/**
	 * Creates a shader program.
	 */
	public ShaderProgram() {
		id = glCreateProgram();
	}
	
	@Override
	public void init() {
		if (isInitialized) {
			return;
		}
		
		isInitialized = true;
		
		// Load shaders
		AssetPools.shaders.addListener(AssetPoolEvent.Type.FAILED, this::handleShaderLoadingError);
		Shader vertexShader = AssetPools.shaders.loadVertexShader(getVertexShaderPath());
		Shader fragmentShader = AssetPools.shaders.loadFragmentShader(getFragmentShaderPath());
		AssetPools.shaders.removeListener(AssetPoolEvent.Type.FAILED, this::handleShaderLoadingError);
		
		// Create shader program
		attachShader(vertexShader);
		attachShader(fragmentShader);
		if (GL.getCapabilities().OpenGL32) {
			bindFragmentDataLocation(0, getOutputVariableName());
		}
		link();
		use();
		
		// Delete linked shaders */
		vertexShader.destroy();
		fragmentShader.destroy();
		
		setupUniforms();
	}
	
	private void handleShaderLoadingError(AssetPoolEvent<Shader> event) {
		String message = String.format("Failed to load shader: %s", event.getPath());
		String error = event.getError();
		if (error != null) {
			message += System.lineSeparator() + error;
		}
		getLogger().error(message, event.getException());
	}
	
	/**
	 * Attach a shader to this program.
	 * @param shader Shader to get attached
	 */
	public void attachShader(Shader shader) {
		glAttachShader(id, shader.getId());
	}
	
	public void bind() {
		use();
		setVertexAttributes(getAttributeNames(), getAttributeSizes(), getStrideLength());
	}
	
	public void setProjectionMatrix(org.joml.Matrix4f projectionMatrix) {
		setUniform(PROJECTION_UNIFORM, projectionMatrix);
	}
	
	public void setViewMatrix(org.joml.Matrix4f viewMatrix) {
		setUniform(VIEW_UNIFORM, viewMatrix);
	}
	
	public void setModelMatrix(org.joml.Matrix4f modelMatrix) {
		setUniform(MODEL_UNIFORM, modelMatrix);
	}
	
	protected abstract String getVertexShaderPath();
	
	protected abstract String getFragmentShaderPath();
	
	protected String getOutputVariableName() {
		return DEFAULT_OUTPUT_VARIABLE;
	}
	
	protected abstract CharSequence[] getAttributeNames();
	
	public int getStrideLength() {
		int stride = 0;
		for (int size : getAttributeSizes()) {
			stride += size;
		}
		return stride;
	}
	
	protected abstract int[] getAttributeSizes();
	
	protected void setupUniforms() {}
	
	public abstract void writeVertex(FloatBuffer buffer, Vertex vertex);
	
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
	
	public void setUniform(CharSequence name, Color value) {
		setUniform(name, value, true);
	}
	
	public void setUniform(int location, Color value) {
		setUniform(location, value, true);
	}
	
	public void setUniform(CharSequence name, Color value, boolean withAlpha) {
		setUniform(requireUniformLocation(name), value, withAlpha);
	}
	
	public void setUniform(int location, Color value, boolean withAlpha) {
		if (withAlpha) {
			setUniform(location, value.toVector4f());
		} else {
			setUniform(location, value.toVector3f());
		}
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
	
	public void setUniform(CharSequence name, org.joml.Matrix2f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, org.joml.Matrix2f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(2 * 2);
			value.get(buffer);
			glUniformMatrix2fv(location, false, buffer);
		}
	}
	
	public void setUniform(CharSequence name, org.joml.Matrix3f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, org.joml.Matrix3f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(3 * 3);
			value.get(buffer);
			glUniformMatrix3fv(location, false, buffer);
		}
	}
	
	public void setUniform(CharSequence name, org.joml.Matrix4f value) {
		setUniform(requireUniformLocation(name), value);
	}
	
	/**
	 * Sets the uniform variable for specified location.
	 * @param location Uniform location
	 * @param value    Value to set
	 */
	public void setUniform(int location, org.joml.Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(4 * 4);
			value.get(buffer);
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
		init();
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
	 * @throws GLException If the program failed to link.
	 */
	public void checkStatus() throws GLException {
		int status = glGetProgrami(id, GL_LINK_STATUS);
		if (status != GL_TRUE) {
			throw new GLException(glGetProgramInfoLog(id));
		}
	}
	
	/**
	 * Deletes the shader program.
	 */
	@Override
	public void destroy() {
		glDeleteProgram(id);
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public Logger getLogger() {
		return logger != null ? logger : Logger.system;
	}
	
	public static int getMaxVertexAttributes() {
		return GLUtils.getInt(GL_MAX_VERTEX_ATTRIBS);
	}
	
	public static UnlitShaderProgram getUnlit() {
		if (unlitShaderProgram != null) {
			return unlitShaderProgram;
		}
		
		unlitShaderProgram = new UnlitShaderProgram();
		return unlitShaderProgram;
	}
	
	public static LitShaderProgram getLit() {
		if (litShaderProgram != null) {
			return litShaderProgram;
		}
		
		litShaderProgram = new LitShaderProgram();
		return litShaderProgram;
	}
	
	public static DepthShaderProgram getDepth() {
		if (depthShaderProgram != null) {
			return depthShaderProgram;
		}
		
		depthShaderProgram = new DepthShaderProgram();
		return depthShaderProgram;
	}
	
	public static void destroyAll() {
		Destructible.destroy(unlitShaderProgram, litShaderProgram, depthShaderProgram);
	}
	
}
