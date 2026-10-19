package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import org.joml.Matrix4f;

public class Matrix4fUniform extends ObjectUniform<Matrix4f> {
	
	public Matrix4fUniform(String name, ShaderProgram program) {
		this(name, program, new Matrix4f());
	}
	
	public Matrix4fUniform(String name, ShaderProgram program, Matrix4f initialValue) {
		super(name, program, initialValue);
	}
	
	@Override
	protected void applyValue(Matrix4f value) {
		program.setUniform(location, value);
	}
	
}
