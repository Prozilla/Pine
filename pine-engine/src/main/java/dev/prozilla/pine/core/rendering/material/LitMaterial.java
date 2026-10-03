package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shader.LitShaderProgram;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class LitMaterial extends Material<LitShaderProgram> {
	
	public Color ambient;
	public Color diffuse;
	public Color specular;
	float reflectance;
	
	public LitMaterial() {
		super(ShaderProgram.getLit());
		ambient = new Color();
		diffuse = new Color();
		specular = new Color();
		reflectance = 1f;
	}
	
	@Override
	public void bind(Renderer renderer) {
		super.bind(renderer);
		shaderProgram.setSurface(ambient, diffuse, specular, reflectance);
	}
	
}
