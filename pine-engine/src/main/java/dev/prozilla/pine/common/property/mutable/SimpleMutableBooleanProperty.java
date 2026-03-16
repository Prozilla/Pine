package dev.prozilla.pine.common.property.mutable;

public class SimpleMutableBooleanProperty implements MutableBooleanProperty {
	
	private boolean value;
	
	public SimpleMutableBooleanProperty() {
		this(false);
	}
	
	/**
	 * Creates a mutable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleMutableBooleanProperty(boolean initialValue) {
		value = initialValue;
	}
	
	@Override
	public boolean set(boolean value) {
		if (this.value == value) {
			return false;
		}
		
		this.value = value;
		return true;
	}
	
	@Override
	public boolean get() {
		return value;
	}
	
}
