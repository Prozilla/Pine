package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Vertex;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;

public class WireframeShaderProgram extends ShaderProgram {
	
	public final static String VERTEX_SHADER_PATH = "/shaders/wireframe.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/wireframe.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 4, 2, 1, 1 };
	
	public static final String LINE_WIDTH_UNIFORM = "uLineWidth";
	public static final String LINE_COLOR_UNIFORM = "uLineColor";
	public static final String FILL_ALPHA_UNIFORM = "uFillAlpha";
	
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
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		super.setProjectionMatrix(projectionMatrix);
		
		setLineWidth(2);
		setLineColor(Color.black());
		setFillAlpha(0.5f);
	}
	
	public void setLineWidth(float width) {
		setUniform(LINE_WIDTH_UNIFORM, width);
	}
	
	public void setLineColor(Color color) {
		setUniform(LINE_COLOR_UNIFORM, color);
	}
	
	public void setFillAlpha(float alpha) {
		setUniform(FILL_ALPHA_UNIFORM, alpha);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.color.getRed()).put(vertex.color.getGreen()).put(vertex.color.getBlue()).put(vertex.color.getAlpha());
		buffer.put(vertex.textureCoordinates.x).put(vertex.textureCoordinates.y);
		buffer.put(vertex.textureId).put(vertex.textureType);
	}
	
}
