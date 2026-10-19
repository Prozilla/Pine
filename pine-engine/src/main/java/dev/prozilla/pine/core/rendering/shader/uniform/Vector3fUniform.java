package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.vector.Vector3fProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Vector3fUniform extends ObjectUniform<Vector3f> implements Vector3fProperty {
	
	public Vector3fUniform(String name, ShaderProgram program) {
		this(name, program, new Vector3f());
	}
	
	public Vector3fUniform(String name, ShaderProgram program, Vector3f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Vector3f value) {
		program.setUniform(location, value);
	}
	
}
