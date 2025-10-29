package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.vector.VectorFloat;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.NonNullProperty;
import dev.prozilla.pine.common.property.vector.Vector2fPropertyBase;
import dev.prozilla.pine.common.util.checks.Checks;

public abstract class DelegatedVector2fPropertyBase<T extends VectorFloat<T>> implements Vector2fPropertyBase<T>, NonNullProperty<T> {
	
	protected final FloatProperty x;
	protected final FloatProperty y;
	
	public DelegatedVector2fPropertyBase(FloatProperty x, FloatProperty y) {
		this.x = Checks.isNotNull(x,"x");
		this.y = Checks.isNotNull(y, "y");
	}
	
	@Override
	public float getX() {
		return x.get();
	}
	
	@Override
	public float getY() {
		return y.get();
	}
	
	@Override
	public FloatProperty xProperty() {
		return x;
	}
	
	@Override
	public FloatProperty yProperty() {
		return y;
	}
	
}
