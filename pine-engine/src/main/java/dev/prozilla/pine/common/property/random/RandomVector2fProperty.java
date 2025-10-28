package dev.prozilla.pine.common.property.random;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import dev.prozilla.pine.common.property.vector.Vector2fProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

public class RandomVector2fProperty extends RandomObjectProperty<Vector2f> implements Vector2fProperty {
	
	public RandomVector2fProperty(float min, float max) {
		this(min, max, min, max);
	}
	
	public RandomVector2fProperty(float minX, float maxX, float minY, float maxY) {
		this(new Vector2f(minX, minY), new Vector2f(maxX, maxY));
	}
	
	public RandomVector2fProperty(Vector2f min, Vector2f max) {
		super(Checks.isNotNull(min, "min"), Checks.isNotNull(max, "max"));
	}
	
	@Contract("-> true")
	@Override
	public boolean isNotNull() {
		return true;
	}
	
	@Override
	public FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.TRUE;
	}
	
	@Override
	public Vector2f getValue() {
		return new Vector2f(getX(), getY());
	}
	
	@Override
	public float getX() {
		return MathUtils.remap(getRandom().nextFloat(), min.x, max.x);
	}
	
	@Override
	public float getY() {
		return MathUtils.remap(getRandom().nextFloat(), min.y, max.y);
	}
	
}
