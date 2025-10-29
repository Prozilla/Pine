package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.Property;
import org.jetbrains.annotations.Contract;

/**
 * A property with a {@link Vector3f} value.
 */
@FunctionalInterface
public interface Vector3fProperty extends Vector3fPropertyBase<Vector3f>, Transmittable<Vector3f> {
	
	@Override
	default void transmit(Vector3f target) {
		Vector3f value = getValue();
		target.set(value.x, value.y, value.z);
	}
	
	@Override
	default float getX() {
		Vector3f value = getValue();
		return value != null ? value.x : 0;
	}
	
	@Override
	default float getY() {
		Vector3f value = getValue();
		return value != null ? value.y : 0;
	}
	
	@Override
	default float getZ() {
		Vector3f value = getValue();
		return value != null ? value.z : 0;
	}
	
	static Vector3fProperty fromProperty(Vector3fProperty property) {
		return property;
	}
	
	@Contract("_ -> new")
	static Vector3fProperty fromProperty(Property<Vector3f> property) {
		return property::getValue;
	}
	
}
