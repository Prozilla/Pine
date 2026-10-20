package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.shader.uniform.FloatUniform;

import java.nio.FloatBuffer;

public class DepthShaderProgram extends ShaderProgram {
	
	public FloatUniform nearUniform;
	public FloatUniform farUniform;
	
	public final static String VERTEX_SHADER_PATH = "/shaders/depth.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/depth.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition" };
	public static final int[] ATTRIBUTE_SIZES = { 3 };
	
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
		super.setupUniforms();
		
		// TODO: automatically read from projection matrix
		nearUniform = new FloatUniform("uNear", this);
		farUniform = new FloatUniform("uFar", this);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
	}
	
}
