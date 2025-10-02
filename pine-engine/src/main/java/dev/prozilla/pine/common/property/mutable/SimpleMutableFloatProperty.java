package dev.prozilla.pine.common.property.mutable;

public class SimpleMutableFloatProperty implements MutableFloatProperty {
	
	private float value;
	
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
		
		onValueChange(this.value, value);
		this.value = value;
		return true;
	}
	
	/**
	 * This method is called whenever the value of this property changes.
	 *
	 * <p>The default implementation does nothing.</p>
	 * @param oldValue The previous value
	 * @param newValue The new value
	 */
	protected void onValueChange(float oldValue, float newValue) {
	
	}
	
	@Override
	public float get() {
		return value;
	}
	
}
