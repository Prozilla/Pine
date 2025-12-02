package dev.prozilla.pine.common.property.animated.transitioned;

import dev.prozilla.pine.common.property.animated.AnimatedPropertyBase;

public interface TransitionedProperty<T> extends AnimatedPropertyBase<T> {
	
	void transitionToValue(T targetValue);
	
}
