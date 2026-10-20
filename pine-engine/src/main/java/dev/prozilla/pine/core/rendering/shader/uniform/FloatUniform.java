package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableFloatProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public class FloatUniform extends SimpleMutableFloatProperty implements Uniform<Float> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public FloatUniform(String name, ShaderProgram program, float initialValue) {
		this(name, program);
		set(initialValue);
	}
	
	public FloatUniform(String name, ShaderProgram program) {
		this(program.getUniformLocation(name), program);
	}
	
	protected FloatUniform(int location, ShaderProgram program) {
		this.location = location;
		this.program = program;
	}
	
	@Override
	public int getLocation() {
		return location;
	}
	
	@Override
	public ShaderProgram getProgram() {
		return program;
	}
	
	@Override
	public boolean set(float value) {
		boolean changed = super.set(value);
		if (changed && location != GL_INVALID_INDEX) {
			program.use();
			program.setUniform(location, value);
		}
		return changed;
	}
	
}
