package dev.prozilla.pine.common.property;

public class ConditionalProperty<T> extends VariableProperty<T> {

	protected final VariableProperty<Boolean> condition;
	protected final VariableProperty<T> propertyTrue;
	protected final VariableProperty<T> propertyFalse;
	
	public ConditionalProperty(VariableProperty<Boolean> condition, T valueTrue, T valueFalse) {
		this(condition, new FixedProperty<>(valueTrue), new FixedProperty<>(valueFalse));
	}
	
	public ConditionalProperty(VariableProperty<Boolean> condition, VariableProperty<T> propertyTrue, VariableProperty<T> propertyFalse) {
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
	public ConditionalProperty<T> invert() {
		return new ConditionalProperty<>(condition, propertyFalse, propertyTrue);
	}
	
}
