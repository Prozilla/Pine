package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.matrix.MatrixFloat;
import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.vector.Vector4fPropertyBase;
import dev.prozilla.pine.common.util.checks.Checks;

public abstract class DelegatedVector4fPropertyBase<T extends VectorFloat<T, M>, M extends MatrixFloat<T, M>> extends DelegatedVector3fPropertyBase<T, M> implements Vector4fPropertyBase<T, M> {
	
	protected final FloatProperty w;
	
	public DelegatedVector4fPropertyBase(FloatProperty x, FloatProperty y, FloatProperty z, FloatProperty w) {
		super(x, y, z);
		this.w = Checks.isNotNull(w, "w");
	}
	
	@Override
	public float getW() {
		return w.get();
	}
	
	@Override
	public FloatProperty wProperty() {
		return w;
	}
	
}
