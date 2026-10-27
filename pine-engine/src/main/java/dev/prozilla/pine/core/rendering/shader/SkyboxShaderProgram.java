package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.material.SkyboxMaterial;
import dev.prozilla.pine.core.rendering.shader.uniform.ColorUniform;
import dev.prozilla.pine.core.rendering.shader.uniform.FloatUniform;
import dev.prozilla.pine.core.rendering.shader.uniform.IntUniform;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glDepthMask;

public class SkyboxShaderProgram extends ShaderProgram {
	
	public IntUniform cubemapUniform;
	
	public ColorUniform fogColorUniform;
	public FloatUniform fogStartUniform;
	public FloatUniform fogEndUniform;
	
	private Vertex vertex;
	
	public final static String VERTEX_SHADER_PATH = "/shaders/skybox.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/skybox.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vColor", "vTexCoords" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 4, 3 };
	
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
		
		cubemapUniform = new IntUniform("uCubemap", this, 0);
		
		fogColorUniform = new ColorUniform("uFogColor", this, Color.gray(), false);
		fogStartUniform = new FloatUniform("uFogStart", this, 0);
		fogEndUniform = new FloatUniform("uFogEnd", this, 0.5f);
	}
	
	public void drawSkybox(Renderer renderer, SkyboxMaterial material) {
		// Prepare render
		renderer.setMaterial(material);
		if (!equals(renderer.getProgram())) {
			material.unbind();
			return;
		}
		renderer.flush();
		
		// Write vertices to buffer
		FloatBuffer buffer = renderer.getVertices();
		writeCube(buffer, material.color);
		
		// Finish render
		glDepthMask(false);
		try {
			renderer.increaseTotalVertices(36);
			renderer.flushVertices(36);
		} finally {
			glDepthMask(true);
			material.unbind();
		}
	}
	
	private void writeCube(FloatBuffer buffer, Color color) {
		if (vertex == null) {
			vertex = new Vertex();
		}
		
		vertex.setColor(color);
		
		float x1 = -1;
		float y1 = -1;
		float z1 = -1;
		float x2 = 1;
		float y2 = 1;
		float z2 = 1;
		
		writeQuad(buffer, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1);
		writeQuad(buffer, x1, y1, z2, x1, y2, z2, x2, y2, z2, x2, y1, z2);
		writeQuad(buffer, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1);
		writeQuad(buffer, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2);
		writeQuad(buffer, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1);
		writeQuad(buffer, x1, y1, z2, x1, y1, z1, x2, y1, z1, x2, y1, z2);
	}
	
	private void writeQuad(FloatBuffer buffer, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		writeVertex(buffer, x1, y1, z1);
		writeVertex(buffer, x2, y2, z2);
		writeVertex(buffer, x3, y3, z3);
		
		writeVertex(buffer, x1, y1, z1);
		writeVertex(buffer, x3, y3, z3);
		writeVertex(buffer, x4, y4, z4);
	}
	
	private void writeVertex(FloatBuffer buffer, float x, float y, float z) {
		vertex.position.set(x, y, z);
		vertex.textureCoordinates.set(x, y, z);
		writeVertex(buffer, vertex);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.color.getRed()).put(vertex.color.getGreen()).put(vertex.color.getBlue()).put(vertex.color.getAlpha());
		buffer.put(vertex.textureCoordinates.x).put(vertex.textureCoordinates.y).put(vertex.textureCoordinates.z);
	}
	
}
