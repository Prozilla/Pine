package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.rendering.Shader;

public final class ShaderPool extends TextAssetPool<Shader> {
	
	private int type;
	
	public Shader loadVertexShader(String path) {
		return load(Shader.Type.VERTEX, path);
	}
	
	public Shader loadFragmentShader(String path) {
		return load(Shader.Type.FRAGMENT, path);
	}
	
	public Shader loadComputeShader(String path) {
		return load(Shader.Type.COMPUTE, path);
	}
	
	public Shader loadGeometryShader(String path) {
		return load(Shader.Type.GEOMETRY, path);
	}
	
	public Shader load(Shader.Type type, String path) {
		return load(type.getValue(), path);
	}
	
	public Shader load(int type, String path) {
		this.type = type;
		return load(path);
	}
	
	@Override
	protected String preprocess(StringBuilder stringBuilder) {
		int nextLineStart = stringBuilder.indexOf("\n") + 1;
		stringBuilder.insert(nextLineStart, Shader.define("PLATFORM", Platform.getOrdinal()));
		for (Platform platform : Platform.values()) {
			stringBuilder.insert(nextLineStart, Shader.define(platform.name(), platform.ordinal()));
		}
		return super.preprocess(stringBuilder);
	}
	
	@Override
	protected Shader parse(String path, String content) {
		return Shader.createShader(type, content);
	}
	
}
