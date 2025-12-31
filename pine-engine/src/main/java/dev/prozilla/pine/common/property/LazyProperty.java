package dev.prozilla.pine.common.property;

/**
 * A property that does not have a value until it is used for the first time.
 *
 * <p>This type of property is optimized, because the value will only be fetched once in most cases.
 * This is especially useful for values that are expensive to fetch.</p>
 */
public abstract class LazyProperty<T> implements Property<T> {
	
	protected T value;
	
	/**
	 * Evaluates the value of this property.
	 *
	 * <p>Implementations of this method should ideally set the value of this property before returning it, to avoid unnecessary fetches.</p>
	 * @return The value this property is supposed to have.
	 */
	protected abstract T fetch();
	
	/**
	 * Returns the current value, or fetches the value if it is {@code null}.
	 * @see #fetch()
	 */
	@Override
	public T getValue() {
		if (value == null) {
			value = fetch();
		}
		return value;
	}
	
}
