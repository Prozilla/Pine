package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableIntProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public class IntUniform extends SimpleMutableIntProperty implements Uniform<Integer> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public IntUniform(String name, ShaderProgram program, int initialValue) {
		this(name, program);
		set(initialValue);
	}
	
	public IntUniform(String name, ShaderProgram program) {
		this(program.getUniformLocation(name), program);
	}
	
	protected IntUniform(int location, ShaderProgram program) {
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
	public boolean set(int value) {
		boolean changed = super.set(value);
		if (changed && location != GL_INVALID_INDEX) {
			program.use();
			program.setUniform(location, value);
		}
		return changed;
	}
	
}
