package dev.prozilla.pine.common.property;

import java.util.Objects;

/**
 * A property whose value can be changed using a setter.
 */
public class MutableProperty<T> extends VariableProperty<T> {
	
	private T value;
	
	/**
	 * Creates a mutable property without an initial value.
	 */
	public MutableProperty() {
		this(null);
	}
	
	/**
	 * Creates a mutable property with an initial value.
	 * @param initialValue The initial value
	 */
	public MutableProperty(T initialValue) {
		value = initialValue;
	}
	
	/**
	 * Sets the value of this property.
	 * @param value The new value
	 * @return {@code true} if the value was changed.
	 */
	public boolean setValue(T value) {
		if (Objects.equals(this.value, value)) {
			return false;
		}
		
		onValueChange(this.value, value);
		this.value = value;
		return true;
	}
	
	/**
	 * This method is called whenever the value of this property changes.
	 *
	 * <p>The default implementation does nothing.</p>
	 * @param oldValue The previous value
	 * @param newValue The new value
	 */
	protected void onValueChange(T oldValue, T newValue) {
	
	}
	
	@Override
	public T getValue() {
		return value;
	}
	
	/**
	 * @return <code>true</code> if the value is not null.
	 */
	public boolean exists() {
		return value != null;
	}
	
}
