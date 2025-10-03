package dev.prozilla.pine.common.property.mutable;

/**
 * A mutable property that supports null values.
 */
public interface MutableObjectProperty<T> extends MutableProperty<T> {
	
	/**
	 * Takes the current value of this property and then sets it to {@code null}.
	 * @return The current value of this property.
	 */
	default T steal() {
		T value = getValue();
		setNull();
		return value;
	}
	
	/**
	 * Sets the value of this property to {@code null}.
	 * @return {@code false} if the value of this property was already {@code null}.
	 */
	default boolean setNull() {
		return setValue(null);
	}
	
}
