package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.rendering.Vertex;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;

public class LitShaderProgram extends ShaderProgram {
	
	public final static String VERTEX_SHADER_PATH = "/shaders/lit.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/lit.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 4, 2, 1, 1 };
	
	// Uniforms
	public static final String SURFACE_UNIFORM = "uSurface";
	public static final String AMBIENT_UNIFORM = SURFACE_UNIFORM + ".ambient";
	public static final String DIFFUSE_UNIFORM = SURFACE_UNIFORM + ".diffuse";
	public static final String SPECULAR_UNIFORM = SURFACE_UNIFORM + ".specular";
	public static final String REFLECTANCE_UNIFORM = SURFACE_UNIFORM + ".reflectance";
	public static final String SUN_LIGHT_UNIFORM = "uSunLight";
	public static final String SUN_LIGHT_COLOR_UNIFORM = SUN_LIGHT_UNIFORM + ".color";
	public static final String SUN_LIGHT_DIRECTION_UNIFORM = SUN_LIGHT_UNIFORM + ".direction";
	public static final String SUN_LIGHT_INTENSITY_UNIFORM = SUN_LIGHT_UNIFORM + ".intensity";
	public static final String SKY_LIGHT_UNIFORM = "uSkyLight";
	public static final String SKY_LIGHT_COLOR_UNIFORM = SKY_LIGHT_UNIFORM + ".color";
	public static final String SKY_LIGHT_INTENSITY_UNIFORM = SKY_LIGHT_UNIFORM + ".intensity";
	
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
		setUniform("uTexture", 0);
		if (Platform.get() != Platform.MACOS) {
			setUniform("uTextureArray", 1);
		}
		
		setProjectionMatrix(new Matrix4f());
		setViewMatrix(new Matrix4f());
		setModelMatrix(new Matrix4f());
		
		setSurface(new Color(), new Color(), new Color(), 1f);
		setSunLight(Color.white(), Vector3f.one(), 1f);
		setSkyLight(Color.white(), 0.25f);
	}
	
	public void setSurface(Color ambient, Color diffuse, Color specular, float reflectance) {
		setUniform(AMBIENT_UNIFORM, ambient);
		setUniform(DIFFUSE_UNIFORM, diffuse);
		setUniform(SPECULAR_UNIFORM, specular);
		setUniform(REFLECTANCE_UNIFORM, reflectance);
	}
	
	// TODO: Replace with UBO + read sunlight from scene
	public void setSunLight(Color color, Vector3f direction, float intensity) {
		setUniform(SUN_LIGHT_COLOR_UNIFORM, color, false);
		setUniform(SUN_LIGHT_DIRECTION_UNIFORM, direction);
		setUniform(SUN_LIGHT_INTENSITY_UNIFORM, intensity);
	}
	
	public void setSkyLight(Color color, float intensity) {
		setUniform(SKY_LIGHT_COLOR_UNIFORM, color, false);
		setUniform(SKY_LIGHT_INTENSITY_UNIFORM, intensity);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.color.getRed()).put(vertex.color.getGreen()).put(vertex.color.getBlue()).put(vertex.color.getAlpha());
		buffer.put(vertex.textureCoordinates.x).put(vertex.textureCoordinates.y);
		buffer.put(vertex.textureId).put(vertex.textureType);
	}
	
}
