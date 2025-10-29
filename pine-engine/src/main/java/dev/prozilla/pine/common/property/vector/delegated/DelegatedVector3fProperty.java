package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.vector.Vector3fProperty;

public class DelegatedVector3fProperty extends DelegatedVector3fPropertyBase<Vector3f> implements Vector3fProperty {
	
	public DelegatedVector3fProperty(FloatProperty x, FloatProperty y, FloatProperty z) {
		super(x, y, z);
	}
	
	@Override
	public Vector3f getValue() {
		return new Vector3f(getX(), getY(), getZ());
	}
	
	@Override
	public void transmit(Vector3f target) {
		target.set(getX(), getY(), getZ());
	}
	
}
