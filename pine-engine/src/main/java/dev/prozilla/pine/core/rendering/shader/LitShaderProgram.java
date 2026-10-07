package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.rendering.Vertex;

import java.nio.FloatBuffer;

public class LitShaderProgram extends ShaderProgram {
	
	public final static String VERTEX_SHADER_PATH = "/shaders/lit.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/lit.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vNormal", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 3, 4, 2, 1, 1 };
	
	// Uniforms
	public static final String SURFACE_UNIFORM = "uSurface";
	public static final String SURFACE_AMBIENT_UNIFORM = SURFACE_UNIFORM + ".ambient";
	public static final String SURFACE_SPECULAR_UNIFORM = SURFACE_UNIFORM + ".specular";
	public static final String SURFACE_REFLECTANCE_UNIFORM = SURFACE_UNIFORM + ".reflectance";
	
	public static final String SUNLIGHT_UNIFORM = "uSunlight";
	public static final String SUNLIGHT_COLOR_UNIFORM = SUNLIGHT_UNIFORM + ".color";
	public static final String SUNLIGHT_DIRECTION_UNIFORM = SUNLIGHT_UNIFORM + ".direction";
	public static final String SUNLIGHT_INTENSITY_UNIFORM = SUNLIGHT_UNIFORM + ".intensity";
	
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
		super.setupUniforms();
		
		setUniform("uTexture", 0);
		if (Platform.get() != Platform.MACOS) {
			setUniform("uTextureArray", 1);
		}
		
		setSurface(new Color(), new Color(), 1f);
		setSunlight(Color.white(), Vector3f.one(), 1f);
		setSkyLight(Color.white(), 0.25f);
	}
	
	public void setSurface(Color ambient, Color specular, float reflectance) {
		setSurfaceAmbient(ambient);
		setSurfaceSpecular(specular);
		setSurfaceReflectance(reflectance);
	}
	
	public void setSurfaceAmbient(Color ambient) {
		setUniform(SURFACE_AMBIENT_UNIFORM, ambient);
	}
	
	public void setSurfaceSpecular(Color specular) {
		setUniform(SURFACE_SPECULAR_UNIFORM, specular);
	}
	
	public void setSurfaceReflectance(float reflectance) {
		setUniform(SURFACE_REFLECTANCE_UNIFORM, reflectance);
	}
	
	// TODO: Replace with UBO + read sunlight from scene
	public void setSunlight(Color color, Vector3f direction, float intensity) {
		setSunlightColor(color);
		setSunlightDirection(direction);
		setSunlightIntensity(intensity);
	}
	
	public void setSunlightColor(Color color) {
		setUniform(SUNLIGHT_COLOR_UNIFORM, color, false);
	}
	
	public void setSunlightDirection(Vector3f direction) {
		setUniform(SUNLIGHT_DIRECTION_UNIFORM, direction);
	}
	
	public void setSunlightIntensity(float intensity) {
		setUniform(SUNLIGHT_INTENSITY_UNIFORM, intensity);
	}
	
	public void setSkyLight(Color color, float intensity) {
		setSkyLightColor(color);
		setSkyLightIntensity(intensity);
	}
	
	public void setSkyLightColor(Color color) {
		setUniform(SKY_LIGHT_COLOR_UNIFORM, color, false);
	}
	
	public void setSkyLightIntensity(float intensity) {
		setUniform(SKY_LIGHT_INTENSITY_UNIFORM, intensity);
	}
	
	@Override
	public void writeVertex(FloatBuffer buffer, Vertex vertex) {
		buffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
		buffer.put(vertex.normal.x).put(vertex.normal.y).put(vertex.normal.z);
		buffer.put(vertex.color.getRed()).put(vertex.color.getGreen()).put(vertex.color.getBlue()).put(vertex.color.getAlpha());
		buffer.put(vertex.textureCoordinates.x).put(vertex.textureCoordinates.y);
		buffer.put(vertex.textureId).put(vertex.textureType);
	}
	
}
