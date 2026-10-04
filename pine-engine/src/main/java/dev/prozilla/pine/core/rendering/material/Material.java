package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Material<S extends ShaderProgram> {

	protected final S shaderProgram;
	
	public TextureAsset texture;
	public Color color;
	
	public Material(S shaderProgram) {
		this(shaderProgram, null, null);
	}
	
	public Material(S shaderProgram, Color color) {
		this(shaderProgram, null, color);
	}
	
	public Material(S shaderProgram, TextureAsset texture) {
		this(shaderProgram, texture, null);
	}
	
	public Material(S shaderProgram, TextureAsset texture, Color color) {
		this.shaderProgram = Checks.isNotNull(shaderProgram, "shaderProgram");
		this.texture = texture;
		this.color = color;
	}
	
	public S getShaderProgram() {
		return shaderProgram;
	}
	
	public void bind(Renderer renderer) {
		renderer.setProgram(shaderProgram);
	}
	
}
