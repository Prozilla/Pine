package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property whose value is determined by a boolean property and a corresponding property for each boolean state.
 *
 * <p>The boolean property determines which property is used to retrieve the value from.</p>
 * @see BooleanProperty#ifElse(Property, Property)
 */
public class ConditionalProperty<T> implements Property<T> {

	protected final BooleanProperty condition;
	protected final Property<T> propertyTrue;
	protected final Property<T> propertyFalse;
	
	/**
	 * Creates a conditional property with possible two values.
	 * @param condition The boolean property that represents the condition
	 * @param valueTrue The value to use when the value of the {@code condition} is {@code true}
	 * @param valueFalse The value to use when the value of the {@code condition} is {@code false}
	 */
	public ConditionalProperty(BooleanProperty condition, T valueTrue, T valueFalse) {
		this(condition, new FixedObjectProperty<>(valueTrue), new FixedObjectProperty<>(valueFalse));
	}
	
	/**
	 * Creates a conditional property.
	 * @param condition The boolean property that represents the condition
	 * @param propertyTrue The property whose value to use when the value of the {@code condition} is {@code true}
	 * @param propertyFalse The property whose value to use when the value of the {@code condition} is {@code false}
	 */
	public ConditionalProperty(BooleanProperty condition, Property<T> propertyTrue, Property<T> propertyFalse) {
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
		return condition.get();
	}
	
	/**
	 * Returns the inverse of this conditional property with the same condition.
	 * @return The inverse of this conditional property.
	 */
	@Contract("-> new")
	public ConditionalProperty<T> invert() {
		return new ConditionalProperty<>(condition, propertyFalse, propertyTrue);
	}
	
}
