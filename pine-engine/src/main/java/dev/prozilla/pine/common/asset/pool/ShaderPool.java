package dev.prozilla.pine.common.asset.pool;

import dev.prozilla.pine.common.system.Platform;
import dev.prozilla.pine.core.rendering.shader.Shader;

import java.util.HashMap;
import java.util.Map;

public final class ShaderPool extends TextAssetPool<Shader> {
	
	private int type;
	private final Map<String, String> macros;
	private final Map<String, String[]> constants;
	
	public ShaderPool() {
		macros = new HashMap<>();
		constants = new HashMap<>();
	}
	
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
		int offset = stringBuilder.indexOf("\n") + 1;
		
		for (Map.Entry<String, String[]> constant : constants.entrySet()) {
			stringBuilder.insert(offset, Shader.constant(constant.getKey(), constant.getValue()[0], constant.getValue()[1]));
		}
		for (Map.Entry<String, String> definition : macros.entrySet()) {
			stringBuilder.insert(offset, Shader.define(definition.getKey(), definition.getValue()));
		}
		
		stringBuilder.insert(offset, Shader.define("PLATFORM", Platform.getOrdinal()));
		for (Platform platform : Platform.values()) {
			stringBuilder.insert(offset, Shader.define(platform.name(), platform.ordinal()));
		}
		
		return super.preprocess(stringBuilder);
	}
	
	@Override
	protected Shader parse(String path, String content) {
		return Shader.createShader(type, content);
	}
	
	@Override
	protected void prepareNext() {
		macros.clear();
		constants.clear();
	}
	
	public void addMacro(String name, String expression) {
		macros.put(name, expression);
	}
	
	public void addConstant(String type, String name, String expression) {
		constants.put(name, new String[]{ type, expression });
	}
	
}
