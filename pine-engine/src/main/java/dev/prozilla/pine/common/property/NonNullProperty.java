package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedBooleanProperty;
import org.jetbrains.annotations.Contract;

/**
 * A property whose value is never {@code null}.
 */
@FunctionalInterface
public interface NonNullProperty<T> extends Property<T> {
	
	/**
	 * Returns this property, whose value is never {@code null}.
	 * @return This property.
	 */
	@Contract("_ -> this")
	@Override
	default NonNullProperty<T> replaceNull(T defaultValue) {
		return this;
	}
	
	/**
	 * Returns {@code true}, because the value of this property is never {@code null}.
	 * @return {@code true}
	 */
	@Contract("-> true")
	@Override
	default boolean isNotNull() {
		return true;
	}
	
	/**
	 * Returns a boolean property whose value is always {@code true}.
	 * @return {@link BooleanProperty#TRUE}
	 */
	@Override
	default FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.TRUE;
	}
	
}
