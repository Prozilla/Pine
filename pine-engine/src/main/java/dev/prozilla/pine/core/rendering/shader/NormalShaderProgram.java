package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.core.rendering.Vertex;

import java.nio.FloatBuffer;

public class NormalShaderProgram extends ShaderProgram {
	
	public final static String VERTEX_SHADER_PATH = "/shaders/normal.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/normal.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vNormal" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 3 };
	
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
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.normal.x).put(vertex.normal.y).put(vertex.normal.z);
	}
	
}
