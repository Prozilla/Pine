package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.rendering.shader.UnlitShaderProgram;

public class UnlitMaterial extends Material<UnlitShaderProgram> {
	
	public UnlitMaterial() {
		this((TextureAsset)null, null);
	}
	
	public UnlitMaterial(Color color) {
		this((TextureAsset)null, color);
	}
	
	public UnlitMaterial(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public UnlitMaterial(TextureAsset texture) {
		this(texture, null);
	}
	
	public UnlitMaterial(String texturePath, Color color) {
		this(AssetPools.textures.load(texturePath), color);
	}
	
	public UnlitMaterial(TextureAsset texture, Color color) {
		super(ShaderProgram.getUnlit(), texture, color);
	}
	
}
