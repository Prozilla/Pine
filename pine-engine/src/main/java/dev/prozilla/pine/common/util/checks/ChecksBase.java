package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidObjectException;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Utility class for chaining checks using functional programming on a named value
 * and throwing a descriptive error if one of the checks is not met.
 */
public abstract class ChecksBase<T, C extends ChecksBase<T, C>> {
	
	protected final T value;
	protected final String name;
	
	public ChecksBase(T value, String name) {
		this.value = value;
		this.name = Objects.requireNonNullElse(name, getDefaultName());
	}
	
	/**
	 * Performs checks on the value as a string.
	 * @param stringChecks The checks to perform
	 */
	public final C asString(Consumer<StringChecks> stringChecks) {
		isNotNull();
		stringChecks.accept(new StringChecks(value.toString(), name + " as string"));
		return self();
	}
	
	/**
	 * Checks that the value is equal to a given object.
	 * @param other The object
	 * @throws InvalidObjectException If the value is not equal to the object
	 */
	public final C isEqualTo(Object other) throws InvalidObjectException {
		isNotNull();
		if (!value.equals(other)) {
			throw new InvalidObjectException(name + " must be equal to " + other);
		}
		return self();
	}
	
	/**
	 * Checks that the value is not equal to a given object.
	 * @param other The object
	 * @throws InvalidObjectException If the value is equal to the object
	 */
	public final C isNotEqualTo(Object other) throws InvalidObjectException {
		isNotNull();
		if (value.equals(other)) {
			throw new InvalidObjectException(name + " must not be equal to " + other);
		}
		return self();
	}
	
	/**
	 * Checks that the value is not null.
	 * @throws InvalidObjectException If the value is {@code null}.
	 */
	public final C isNotNull() throws InvalidObjectException {
		if (value == null) {
			throw new InvalidObjectException(name + " must not be null");
		}
		return self();
	}
	
	protected abstract C self();
	
	protected abstract String getDefaultName();
	
}
