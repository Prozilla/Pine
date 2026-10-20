package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.math.matrix.Matrix3f;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Matrix3fUniform extends ObjectUniform<Matrix3f> {
	
	public Matrix3fUniform(String name, ShaderProgram program) {
		super(name, program);
	}
	
	public Matrix3fUniform(String name, ShaderProgram program, Matrix3f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Matrix3f value) {
		program.setUniform(location, value);
	}
	
}
