package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shader.LitShaderProgram;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class LitMaterial extends Material<LitShaderProgram> {
	
	public Color ambient;
	public Color diffuse;
	public Color specular;
	public float reflectance;
	
	public LitMaterial() {
		this((TextureAsset)null, null);
	}
	
	public LitMaterial(Color color) {
		this((TextureAsset)null, color);
	}
	
	public LitMaterial(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public LitMaterial(TextureAsset texture) {
		this(texture, null);
	}
	
	public LitMaterial(String texturePath, Color color) {
		this(AssetPools.textures.load(texturePath), color);
	}
	
	public LitMaterial(TextureAsset texture, Color color) {
		super(ShaderProgram.getLit(), texture, color);
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
