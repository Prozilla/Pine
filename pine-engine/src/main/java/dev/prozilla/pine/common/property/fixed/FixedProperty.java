package dev.prozilla.pine.common.property.fixed;

import dev.prozilla.pine.common.property.*;
import dev.prozilla.pine.common.property.compared.ComparedProperty;
import dev.prozilla.pine.common.util.StringUtils;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

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
	default FixedBooleanProperty hasValueProperty(T value) {
		return BooleanProperty.fromValue(hasValue(value));
	}
	
	/**
	 * Returns this property, because its value is always the same, so it is effectively the same as a snapshot.
	 * @return This property.
	 */
	@Contract("-> this")
	@Override
	default FixedProperty<T> snapshot() {
		return this;
	}
	
	@Override
	default IntProperty compareWith(Comparator<T> comparator, Property<T> other) {
		return other.compareWith(comparator, this).mapToInt((value) -> -value);
	}
	
	/**
	 * Compares this property with another fixed property.
	 *
	 * <p>The resulting property is a {@link FixedIntProperty} instead of a {@link ComparedProperty}, because two fixed properties only need to be compared once, as their values never change.</p>
	 * @param comparator The comparing function
	 * @param other The property to compare with
	 * @return A fixed integer property whose value is based on the comparison of the two properties.
	 */
	@Override
	default FixedIntProperty compareWith(Comparator<T> comparator, FixedProperty<T> other) {
		return new FixedIntProperty(comparator.compare(getValue(), other.getValue()));
	}
	
	@Override
	default FixedProperty<T> replaceNull(T defaultValue) {
		if (isNotNull()) {
			return this;
		} else {
			return new FixedObjectProperty<>(defaultValue);
		}
	}
	
	@Override
	default FixedBooleanProperty derive(BooleanProperty property) {
		return property.snapshot();
	}
	
	@Override
	default FixedFloatProperty derive(FloatProperty property) {
		return property.snapshot();
	}
	
	@Override
	default FixedIntProperty derive(IntProperty property) {
		return property.snapshot();
	}
	
	@Override
	default FixedStringProperty derive(StringProperty property) {
		return property.snapshot();
	}
	
	@Override
	default <S> FixedProperty<S> derive(Property<S> property) {
		return property.snapshot();
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
