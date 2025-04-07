package dev.prozilla.pine.common.property.style;

import dev.prozilla.pine.common.property.adaptive.AdaptiveProperty;

@FunctionalInterface
public interface StyledPropertyEventListener<T> {
	
	void onChange(AdaptiveProperty<T> oldProperty, AdaptiveProperty<T> newProperty);
	
}
