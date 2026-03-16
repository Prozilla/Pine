package dev.prozilla.pine.common.property.mutable;

public class SimpleMutableIntProperty implements MutableIntProperty {
	
	private int value;
	
	public SimpleMutableIntProperty() {
		this(0);
	}
	
	/**
	 * Creates a mutable property with an initial value.
	 * @param initialValue The initial value
	 */
	public SimpleMutableIntProperty(int initialValue) {
		value = initialValue;
	}
	
	@Override
	public boolean set(int value) {
		if (this.value == value) {
			return false;
		}
		
		this.value = value;
		return true;
	}
	
	@Override
	public int get() {
		return value;
	}
	
}
