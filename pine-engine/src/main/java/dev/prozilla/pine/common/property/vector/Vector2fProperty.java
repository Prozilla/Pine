package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.Property;
import org.jetbrains.annotations.Contract;

public interface Vector2fProperty extends Vector2fPropertyBase<Vector2f> {
	
	@Override
	default float getX() {
		Vector2f value = getValue();
		return value != null ? value.x : 0;
	}
	
	@Override
	default float getY() {
		Vector2f value = getValue();
		return value != null ? value.y : 0;
	}
	
	static Vector2fProperty fromProperty(Vector2fProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static Vector2fProperty fromProperty(Property<Vector2f> property) {
		return property::getValue;
	}
	
}
