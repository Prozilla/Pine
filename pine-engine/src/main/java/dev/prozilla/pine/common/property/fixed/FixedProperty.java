package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.StringUtils;

/**
 * A property with a value that never changes.
 */
@FunctionalInterface
public interface FixedProperty<T> extends Property<T> {
	
	@Override
	default FixedStringProperty toStringProperty() {
		return new FixedStringProperty(StringUtils.toString(getValue()));
	}
	
	@Override
	default FixedBooleanProperty isNotNullProperty() {
		return BooleanProperty.fromValue(isNotNull());
	}
	
	@Override
	default FixedProperty<T> replaceNull(T defaultValue) {
		if (isNotNull()) {
			return this;
		} else {
			return new FixedObjectProperty<>(defaultValue);
		}
	}
	
	/**
	 * Creates a new fixed property based on a given value.
	 * @param value A value or {@code null}
	 * @return A {@link FixedObjectProperty} if {@code value} is not {@code null}, otherwise a {@link NullProperty}.
	 * @param <T> The type of value
	 */
	static <T> FixedProperty<T> fromValue(T value) {
		if (value == null) {
			return new NullProperty<>();
		} else {
			return new FixedObjectProperty<>(value);
		}
	}
	
}
