package dev.prozilla.pine.core.rendering.shader;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.light.SunLight;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.shader.uniform.ColorUniform;
import dev.prozilla.pine.core.rendering.shader.uniform.FloatUniform;
import dev.prozilla.pine.core.rendering.shader.uniform.Vector3fUniform;

import java.nio.FloatBuffer;

public class LitShaderProgram extends ShaderProgram {
	
	public ColorUniform surfaceAmbientUniform;
	public ColorUniform surfaceSpecularUniform;
	public FloatUniform surfaceReflectanceUniform;
	
	public ColorUniform sunlightColorUniform;
	public Vector3fUniform sunlightDirectionUniform;
	public FloatUniform sunlightIntensityUniform;
	
	public ColorUniform skyLightColorUniform;
	public FloatUniform skyLightIntensityUniform;
	
	public ColorUniform fogColorUniform;
	public FloatUniform fogDensityUniform;
	
	public final static String VERTEX_SHADER_PATH = "/shaders/lit.vert";
	public final static String FRAGMENT_SHADER_PATH = "/shaders/lit.frag";
	
	public static final CharSequence[] ATTRIBUTE_NAMES = { "vPosition", "vNormal", "vColor", "vTexCoords", "vTexId", "vIsArrayTexture" };
	public static final int[] ATTRIBUTE_SIZES = { 3, 3, 4, 2, 1, 1 };
	
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
		
		surfaceAmbientUniform = new ColorUniform("uSurface.ambient", this, Color.white());
		surfaceSpecularUniform = new ColorUniform("uSurface.specular", this, Color.white());
		surfaceReflectanceUniform = new FloatUniform("uSurface.reflectance", this, 1f);

		sunlightColorUniform = new ColorUniform("uSunlight.color", this, Color.white(), false);
		sunlightDirectionUniform = new Vector3fUniform("uSunlight.direction", this, Vector3f.one());
		sunlightIntensityUniform = new FloatUniform("uSunlight.intensity", this, 1f);

		skyLightColorUniform = new ColorUniform("uSkyLight.color", this, Color.white(), false);
		skyLightIntensityUniform = new FloatUniform("uSkyLight.intensity", this, 0.25f);
		
		fogColorUniform = new ColorUniform("uFog.color", this, Color.gray(), false);
		fogDensityUniform = new FloatUniform("uFog.density", this, 0.01f);
	}
	
	@Override
	public void setRenderer(Renderer renderer) {
		super.setRenderer(renderer);
		
		SunLight sunLight = renderer.getApplication().getCurrentScene().getSunLight();
		if (sunLight != null) {
			setSunlight(sunLight.color, sunLight.getDirection(), sunLight.intensity);
		}
	}
	
	public void setSurface(Color ambient, Color specular, float reflectance) {
		surfaceAmbientUniform.setValue(ambient);
		surfaceSpecularUniform.setValue(specular);
		surfaceReflectanceUniform.set(reflectance);
	}
	
	// TODO: Replace with UBO + read sunlight from scene
	public void setSunlight(Color color, Vector3f direction, float intensity) {
		sunlightColorUniform.setValue(color);
		sunlightDirectionUniform.setValue(direction);
		sunlightIntensityUniform.set(intensity);
	}
	
	public void setSkyLight(Color color, float intensity) {
		skyLightColorUniform.setValue(color);
		skyLightIntensityUniform.set(intensity);
	}
	
	public void setFog(Color color, float density) {
		fogColorUniform.setValue(color);
		fogDensityUniform.set(density);
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
