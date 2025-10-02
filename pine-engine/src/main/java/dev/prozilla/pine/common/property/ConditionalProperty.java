package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property whose value is determined by a boolean property and a corresponding property for each boolean state.
 *
 * <p>The boolean property determines which property is used to retrieve the value from.</p>
 */
public class ConditionalProperty<T> implements Property<T> {

	protected final Property<Boolean> condition;
	protected final Property<T> propertyTrue;
	protected final Property<T> propertyFalse;
	
	public ConditionalProperty(Property<Boolean> condition, T valueTrue, T valueFalse) {
		this(condition, new FixedObjectProperty<>(valueTrue), new FixedObjectProperty<>(valueFalse));
	}
	
	public ConditionalProperty(Property<Boolean> condition, Property<T> propertyTrue, Property<T> propertyFalse) {
		this.condition = condition;
		this.propertyTrue = propertyTrue;
		this.propertyFalse = propertyFalse;
	}
	
	@Override
	public T getValue() {
		if (isTrue()) {
			return propertyTrue.getValue();
		} else {
			return propertyFalse.getValue();
		}
	}
	
	/**
	 * Checks if the condition of this property is not {@code true}.
	 * @return {@code true} if the condition is not {@code true}.
	 */
	public boolean isFalse() {
		return !isTrue();
	}
	
	/**
	 * Checks if the condition of this property is {@code true}.
	 * @return {@code true} if the condition is {@code true}.
	 */
	public boolean isTrue() {
		return condition.getValue();
	}
	
	/**
	 * Returns the inverse of this conditional property.
	 * @return The inverse of this conditional property
	 */
	@Contract("-> new")
	public ConditionalProperty<T> invert() {
		return new ConditionalProperty<>(condition, propertyFalse, propertyTrue);
	}
	
}
