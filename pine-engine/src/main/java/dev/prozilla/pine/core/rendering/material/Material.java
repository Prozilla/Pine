package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Material<S extends ShaderProgram> {

	protected final S shaderProgram;
	
	public Material(S shaderProgram) {
		this.shaderProgram = Checks.isNotNull(shaderProgram, "shaderProgram");
	}
	
	public S getShaderProgram() {
		return shaderProgram;
	}
	
	public void bind(Renderer renderer) {
		renderer.setProgram(shaderProgram);
	}
	
}
