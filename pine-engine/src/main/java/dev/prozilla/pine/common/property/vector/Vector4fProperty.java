package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.Property;
import org.jetbrains.annotations.Contract;

public interface Vector4fProperty extends Vector4fPropertyBase<Vector4f> {
	
	@Override
	default float getX() {
		Vector4f value = getValue();
		return value != null ? value.x : 0;
	}
	
	@Override
	default float getY() {
		Vector4f value = getValue();
		return value != null ? value.y : 0;
	}
	
	@Override
	default float getZ() {
		Vector4f value = getValue();
		return value != null ? value.z : 0;
	}
	
	@Override
	default float getW() {
		Vector4f value = getValue();
		return value != null ? value.w : 0;
	}
	
	static Vector4fProperty fromProperty(Vector4fProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static Vector4fProperty fromProperty(Property<Vector4f> property) {
		return property::getValue;
	}
	
}
