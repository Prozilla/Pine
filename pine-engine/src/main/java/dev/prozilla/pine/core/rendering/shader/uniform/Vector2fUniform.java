package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.vector.Vector2fProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Vector2fUniform extends ObjectUniform<Vector2f> implements Vector2fProperty {
	
	public Vector2fUniform(String name, ShaderProgram program) {
		super(name, program);
	}
	
	public Vector2fUniform(String name, ShaderProgram program, Vector2f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Vector2f value) {
		program.setUniform(location, value);
	}
	
}
