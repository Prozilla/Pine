package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class ColorUniform extends ObjectUniform<Color> implements ColorProperty {
	
	public ColorUniform(String name, ShaderProgram program) {
		this(name, program, Color.black());
	}
	
	public ColorUniform(String name, ShaderProgram program, Color initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Color value) {
		program.setUniform(location, value);
	}
	
}
