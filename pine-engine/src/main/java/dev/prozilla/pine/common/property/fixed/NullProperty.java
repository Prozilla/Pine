package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import org.jetbrains.annotations.Contract;

/**
 * A property whose value is always {@code null}.
 */
public class NullProperty<T> implements FixedProperty<T> {
	
	/**
	 * Returns the {@code defaultValue}, because the value of this property is always {@code null}.
	 * @param defaultValue The value to return
	 * @return The {@code defaultValue}.
	 */
	@Contract("null -> fail; !null -> param1")
	@Override
	public T getValueOr(T defaultValue) {
		return Checks.isNotNull(defaultValue, "defaultValue");
	}
	
	/**
	 * Returns {@code null}.
	 * @return {@code null}
	 */
	@Contract("-> null")
	@Override
	public T getValue() {
		return null;
	}
	
	/**
	 * Returns {@code false}, because the value of this property is always {@code null}.
	 * @return {@code false}
	 */
	@Contract("-> false")
	@Override
	public boolean isNotNull() {
		return false;
	}
	
	@Contract("-> this")
	@Override
	public NullProperty<T> snapshot() {
		return this;
	}
	
	public FixedColorProperty replaceNull(Color defaultValue) {
		return new FixedColorProperty(defaultValue);
	}
	
	public FixedStringProperty replaceNull(String defaultValue) {
		return new FixedStringProperty(defaultValue);
	}

	@Override
	public FixedObjectProperty<T> replaceNull(T defaultValue) {
		return new FixedObjectProperty<>(defaultValue);
	}
	
}
