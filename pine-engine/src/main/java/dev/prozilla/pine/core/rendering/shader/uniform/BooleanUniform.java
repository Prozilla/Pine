package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableBooleanProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public class BooleanUniform extends SimpleMutableBooleanProperty implements Uniform<Boolean> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public BooleanUniform(String name, ShaderProgram program) {
		this(name, program, false);
	}
	
	public BooleanUniform(String name, ShaderProgram program, boolean initialValue) {
		this(program.getUniformLocation(name), program, initialValue);
	}
	
	protected BooleanUniform(int location, ShaderProgram program, boolean initialValue) {
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
	public boolean set(boolean value) {
		boolean changed =  super.set(value);
		if (changed && location != GL_INVALID_INDEX) {
			program.setUniform(location, value);
		}
		return changed;
	}
	
}
