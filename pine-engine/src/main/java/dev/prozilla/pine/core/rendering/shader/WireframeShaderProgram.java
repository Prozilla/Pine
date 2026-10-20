package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.shader.uniform.ColorUniform;
import dev.prozilla.pine.core.rendering.shader.uniform.FloatUniform;

import java.nio.FloatBuffer;

public class WireframeShaderProgram extends ShaderProgram {
	
	public FloatUniform lineWidthUniform;
	public ColorUniform lineColorUniform;
	public FloatUniform fillAlphaUniform;
	
	public final static String VERTEX_SHADER_PATH = "/shaders/wireframe.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/wireframe.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 4, 2, 1, 1 };
	
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
		
		lineWidthUniform = new FloatUniform("uLineWidth", this, 2);
		lineColorUniform = new ColorUniform("uLineColor", this, Color.black());
		fillAlphaUniform = new FloatUniform("uFillAlpha", this, 0.5f);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.color.getRed()).put(vertex.color.getGreen()).put(vertex.color.getBlue()).put(vertex.color.getAlpha());
		buffer.put(vertex.textureCoordinates.x).put(vertex.textureCoordinates.y);
		buffer.put(vertex.textureId).put(vertex.textureType);
	}
	
}
