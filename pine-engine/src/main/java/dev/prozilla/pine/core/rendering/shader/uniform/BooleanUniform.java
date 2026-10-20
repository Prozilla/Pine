package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableBooleanProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public class BooleanUniform extends SimpleMutableBooleanProperty implements Uniform<Boolean> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public BooleanUniform(String name, ShaderProgram program, boolean initialValue) {
		this(name, program);
		set(initialValue);
	}
	
	public BooleanUniform(String name, ShaderProgram program) {
		this(program.getUniformLocation(name), program);
	}
	
	protected BooleanUniform(int location, ShaderProgram program) {
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
		boolean changed = super.set(value);
		if (changed && location != GL_INVALID_INDEX) {
			program.use();
			program.setUniform(location, value);
		}
		return changed;
	}
	
}
