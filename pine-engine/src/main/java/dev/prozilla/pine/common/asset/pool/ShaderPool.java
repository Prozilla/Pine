package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.core.rendering.Shader;

public final class ShaderPool extends TextAssetPool<Shader> {
	
	private int type;
	
	public Shader load(int type, String path) {
		this.type = type;
		return load(path);
	}
	
	@Override
	protected Shader parse(String path, String content) {
		return Shader.createShader(type, content);
	}
	
}
