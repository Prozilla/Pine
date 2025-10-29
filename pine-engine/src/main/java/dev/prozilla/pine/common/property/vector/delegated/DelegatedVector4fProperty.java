package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.vector.Vector4f;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.vector.Vector4fProperty;

public class DelegatedVector4fProperty extends DelegatedVector4fPropertyBase<Vector4f> implements Vector4fProperty {
	
	public DelegatedVector4fProperty(FloatProperty x, FloatProperty y, FloatProperty z, FloatProperty w) {
		super(x, y, z, w);
	}
	
	@Override
	public Vector4f getValue() {
		return new Vector4f(getX(), getY(), getZ(), getW());
	}
	
	@Override
	public void transmit(Vector4f target) {
		target.set(getX(), getY(), getZ(), getW());
	}
	
}
