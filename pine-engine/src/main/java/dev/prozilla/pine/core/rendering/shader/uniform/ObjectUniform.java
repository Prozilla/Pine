package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public abstract class ObjectUniform<T> extends SimpleMutableObjectProperty<T> implements Uniform<T> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public ObjectUniform(String name, ShaderProgram program) {
		this(name, program, null);
	}
	
	public ObjectUniform(String name, ShaderProgram program, T initialValue) {
		this(program.getUniformLocation(name), program, initialValue);
	}
	
	protected ObjectUniform(int location, ShaderProgram program, T initialValue) {
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
	public boolean setValue(T value) {
		boolean changed =  super.setValue(value);
		if (changed && location != GL_INVALID_INDEX) {
			applyValue(value);
		}
		return changed;
	}
	
	protected abstract void applyValue(T value);
	
}
