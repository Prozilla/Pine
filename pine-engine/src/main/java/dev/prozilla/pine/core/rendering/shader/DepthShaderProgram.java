package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.core.rendering.Vertex;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;

public class DepthShaderProgram extends ShaderProgram {
	
	public final static String VERTEX_SHADER_PATH = "/shaders/depth.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/depth.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition" };
	public static final int[] ATTRIBUTE_SIZES = { 3 };
	
	public static final String NEAR_UNIFORM = "uNear";
	public static final String FAR_UNIFORM = "uFar";
	
	@Override
	protected String getVertexShaderPath() {
		return VERTEX_SHADER_PATH;
	}
	
	@Override
	protected String getFragmentShaderPath() {
		return FRAGMENT_SHADER_PATH;
	}
	
	@Override
	protected CharSequence[] getAttributeNames() {
		return ATTRIBUTE_NAMES;
	}
	
	@Override
	protected int[] getAttributeSizes() {
		return ATTRIBUTE_SIZES;
	}
	
	@Override
	protected void setupUniforms() {
		setProjectionMatrix(new Matrix4f());
		setViewMatrix(new Matrix4f());
		setModelMatrix(new Matrix4f());
	}
	
	@Override
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		super.setProjectionMatrix(projectionMatrix);
		
		setNear(projectionMatrix.perspectiveNear());
		setFar(projectionMatrix.perspectiveFar());
	}
	
	public void setNear(float near) {
		setUniform(NEAR_UNIFORM, near);
	}
	
	public void setFar(float far) {
		setUniform(FAR_UNIFORM, far);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
	}
	
}
