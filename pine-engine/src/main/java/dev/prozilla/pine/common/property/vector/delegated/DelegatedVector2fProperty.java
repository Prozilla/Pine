package dev.prozilla.pine.common.property.vector.delegated;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.vector.Vector2fProperty;

public class DelegatedVector2fProperty extends DelegatedVector2fPropertyBase<Vector2f> implements Vector2fProperty {
	
	/**
	 * Creates a {@link Vector2fProperty} by combining two float properties.
	 * @param x The x property
	 * @param y The y property
	 */
	public DelegatedVector2fProperty(FloatProperty x, FloatProperty y) {
		super(x, y);
	}
	
	@Override
	public Vector2f getValue() {
		return new Vector2f(getX(), getY());
	}
	
	@Override
	public void transmit(Vector2f target) {
		target.set(getX(), getY());
	}
	
}
