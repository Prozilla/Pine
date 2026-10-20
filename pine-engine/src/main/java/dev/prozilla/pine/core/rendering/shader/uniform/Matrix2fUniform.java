package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.math.matrix.Matrix2f;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

public class Matrix2fUniform extends ObjectUniform<Matrix2f> {
	
	public Matrix2fUniform(String name, ShaderProgram program) {
		super(name, program);
	}
	
	public Matrix2fUniform(String name, ShaderProgram program, Matrix2f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Matrix2f value) {
		program.setUniform(location, value);
	}
	
}
