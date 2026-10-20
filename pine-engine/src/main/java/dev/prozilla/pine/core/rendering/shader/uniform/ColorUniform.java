package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class ColorUniform extends ObjectUniform<Color> implements ColorProperty {
	
	protected final boolean withAlpha;
	
	public ColorUniform(String name, ShaderProgram program) {
		super(name, program);
		withAlpha = true;
	}
	
	public ColorUniform(String name, ShaderProgram program, Color initialValue) {
		this(name, program, initialValue, true);
	}
	
	public ColorUniform(String name, ShaderProgram program, Color initialValue, boolean withAlpha) {
		super(name, program);
		this.withAlpha = withAlpha;
		setValue(initialValue);
	}
	
	@Override
	protected void applyValue(Color value) {
		program.setUniform(location, value, withAlpha);
	}
	
}
