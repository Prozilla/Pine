package dev.prozilla.pine.common.property.vector;

import dev.prozilla.pine.common.Transmittable;
import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.Property;
import org.jetbrains.annotations.Contract;

/**
 * A property with a {@link Vector4f} value.
 */
@FunctionalInterface
public interface Vector4fProperty extends Vector4fPropertyBase<Vector4f>, Transmittable<Vector4f> {
	
	@Override
	default void transmit(Vector4f target) {
		Vector4f value = getValue();
		target.set(value.x, value.y, value.z, value.w);
	}
	
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
