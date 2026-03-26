package dev.prozilla.pine.common.property.mutable;

public class SimpleMutableFloatProperty implements MutableFloatProperty {
	
	private float value;
	
	public SimpleMutableFloatProperty() {
		this(0);
	}
	
	/**
	 * Creates a mutable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleMutableFloatProperty(float initialValue) {
		value = initialValue;
	}
	
	@Override
	public boolean set(float value) {
		if (this.value == value) {
			return false;
		}
		
		this.value = value;
		return true;
	}
	
	@Override
	public float get() {
		return value;
	}
	
}
