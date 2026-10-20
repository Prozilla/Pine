package dev.prozilla.pine.core.rendering.shader.uniform;

import dev.prozilla.pine.common.Transferable;
import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;

import java.util.Objects;

import static org.lwjgl.opengl.GL31.GL_INVALID_INDEX;

public abstract class ObjectUniform<T extends Transferable<T>> extends SimpleMutableObjectProperty<T> implements Uniform<T> {
	
	protected final int location;
	protected final ShaderProgram program;
	
	public ObjectUniform(String name, ShaderProgram program, T initialValue) {
		this(name, program);
		setValue(initialValue);
	}
	
	public ObjectUniform(String name, ShaderProgram program) {
		this(program.getUniformLocation(name), program);
	}
	
	protected ObjectUniform(int location, ShaderProgram program) {
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
		if (Objects.equals(this.value, value)) {
			return false;
		}
		
		if (this.value == null) {
			this.value = value.clone();
		} else if (value != null) {
			value.transmit(this.value);
		} else {
			this.value = null;
		}
		
		if (value != null && location != GL_INVALID_INDEX) {
			program.use();
			applyValue(value);
		}
		
		return true;
	}
	
	/**
	 * Applies the value of this uniform to the associated shader program.
	 * @param value The value of this uniform.
	 */
	protected abstract void applyValue(T value);
	
}
