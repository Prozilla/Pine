package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidArrayException;
import dev.prozilla.pine.common.exception.InvalidObjectException;

import java.util.function.Consumer;

/**
 * Utility class for performing checks on arrays.
 */
public final class ArrayChecks<E> extends ChecksBase<E[], ArrayChecks<E>> implements IterableChecks<ArrayChecks<E>> {
	
	public ArrayChecks(E[] value, String name) {
		super(value, name);
	}
	
	@Override
	public ArrayChecks<E> hasLength(Consumer<IntChecks> lengthChecks) throws InvalidObjectException {
		isNotNull();
		lengthChecks.accept(new IntChecks(value.length, "Length of " + name));
		return this;
	}
	
	@Override
	public ArrayChecks<E> isNotEmpty() throws InvalidArrayException {
		isNotNull();
		Checks.isNotEmpty(value, name + " must not be empty");
		return this;
	}
	
	@Override
	protected ArrayChecks<E> self() {
		return this;
	}
	
	@Override
	protected String getDefaultName() {
		return "array";
	}
}
