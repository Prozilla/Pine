package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableIntProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public class IntUniform extends SimpleMutableIntProperty implements Uniform<Integer> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public IntUniform(String name, ShaderProgram program) {
		this(name, program, 0);
	}
	
	public IntUniform(String name, ShaderProgram program, int initialValue) {
		this(program.getUniformLocation(name), program, initialValue);
	}
	
	protected IntUniform(int location, ShaderProgram program, int initialValue) {
		super(initialValue);
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
		boolean changed =  super.set(value);
		if (changed && location != GL_INVALID_INDEX) {
			program.setUniform(location, value);
		}
		return changed;
	}
	
}
