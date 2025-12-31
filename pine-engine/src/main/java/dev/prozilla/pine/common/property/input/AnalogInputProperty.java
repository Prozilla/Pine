package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;
import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;

public abstract class AnalogInputProperty<T> extends SimpleMutableObjectProperty<T> {
	
	private float threshold;
	
	/**
	 * Creates a new analog input property.
	 * @param initialValue The initial value
	 * @param threshold The threshold for input registration
	 */
	public AnalogInputProperty(T initialValue, float threshold) {
		super(initialValue);
		this.threshold = threshold;
	}
	
	/**
	 * Returns the threshold for input registration of this property.
	 * @return The threshold for input registration of this property.
	 */
	public float getThreshold() {
		return threshold;
	}
	
	/**
	 * Sets the threshold for input registration of this property.
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Returns a property whose value is bound to the threshold of this property.
	 * @return A property whose value is bound to the threshold of this property.
	 */
	public MutableFloatProperty thresholdProperty() {
		return new MutableFloatProperty() {
			@Override
			public boolean set(float value) {
				if (threshold == value) {
					return false;
				}
				threshold = value;
				return true;
			}
			
			@Override
			public float get() {
				return threshold;
			}
		};
	}
	
}
