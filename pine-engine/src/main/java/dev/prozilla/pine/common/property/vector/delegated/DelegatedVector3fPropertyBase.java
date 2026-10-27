package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.matrix.MatrixFloat;
import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.vector.Vector3fPropertyBase;
import dev.prozilla.pine.common.util.checks.Checks;

public abstract class DelegatedVector3fPropertyBase<T extends VectorFloat<T, M>, M extends MatrixFloat<T, M>> extends DelegatedVector2fPropertyBase<T, M> implements Vector3fPropertyBase<T, M> {
	
	protected final FloatProperty z;
	
	public DelegatedVector3fPropertyBase(FloatProperty x, FloatProperty y, FloatProperty z) {
		super(x, y);
		this.z = Checks.isNotNull(z, "z");
	}
	
	@Override
	public float getZ() {
		return z.get();
	}
	
	@Override
	public FloatProperty zProperty() {
		return z;
	}
	
}
