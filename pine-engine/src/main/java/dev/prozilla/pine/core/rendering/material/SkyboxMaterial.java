package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.asset.image.Cubemap;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.rendering.shader.SkyboxShaderProgram;

public class SkyboxMaterial extends Material<SkyboxShaderProgram> {
	
	public Cubemap cubemap;
	
	public SkyboxMaterial() {
		this((Cubemap)null, null);
	}
	
	public SkyboxMaterial(Color color) {
		this((Cubemap)null, color);
	}
	
	public SkyboxMaterial(String cubemapPath) {
		this(AssetPools.cubemaps.load(cubemapPath));
	}
	
	public SkyboxMaterial(Cubemap cubemap) {
		this(cubemap, Color.white());
	}
	
	public SkyboxMaterial(String cubemapPath, Color color) {
		this(AssetPools.cubemaps.load(cubemapPath), color);
	}
	
	public SkyboxMaterial(Cubemap cubemap, Color color) {
		super(ShaderProgram.getSkybox(), null, color);
		this.cubemap = cubemap;
	}
	
	@Override
	public void bind() {
		super.bind();
		if (cubemap != null) {
			cubemap.bind();
		}
	}
	
	public void unbind() {
		if (cubemap != null) {
			cubemap.unbind();
		}
	}
	
	public boolean hasCubemap() {
		return cubemap != null;
	}
	
}
