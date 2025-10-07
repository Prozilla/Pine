package dev.prozilla.pine.common.property.input;

import dev.prozilla.pine.common.property.mutable.MutableFloatProperty;
import dev.prozilla.pine.common.property.mutable.SimpleMutableObjectProperty;

public abstract class AnalogInputProperty<T> extends SimpleMutableObjectProperty<T> {
	
	private float threshold;
	
	public AnalogInputProperty(T initialValue, float treshold) {
		super(initialValue);
		this.threshold = treshold;
	}
	
	public float getThreshold() {
		return threshold;
	}
	
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}
	
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
