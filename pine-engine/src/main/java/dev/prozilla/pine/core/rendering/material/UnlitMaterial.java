package dev.prozilla.pine.core.rendering.material;

import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.rendering.shader.UnlitShaderProgram;

public class UnlitMaterial extends Material<UnlitShaderProgram> {
	
	public UnlitMaterial() {
		super(ShaderProgram.getUnlit());
	}
	
}
