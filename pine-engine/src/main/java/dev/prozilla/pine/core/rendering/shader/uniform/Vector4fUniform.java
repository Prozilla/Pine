package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.vector.Vector4fProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Vector4fUniform extends ObjectUniform<Vector4f> implements Vector4fProperty {
	
	public Vector4fUniform(String name, ShaderProgram program) {
		this(name, program, new Vector4f());
	}
	
	public Vector4fUniform(String name, ShaderProgram program, Vector4f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Vector4f value) {
		program.setUniform(location, value);
	}
	
}
