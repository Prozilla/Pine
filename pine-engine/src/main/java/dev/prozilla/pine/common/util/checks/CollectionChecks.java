package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidCollectionException;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Utility class for performing checks on collections.
 */
public final class CollectionChecks<E> extends ChecksBase<Collection<E>, CollectionChecks<E>> implements IterableChecks<CollectionChecks<E>> {
	
	public CollectionChecks(Collection<E> value, String name) {
		super(value, name);
	}
	
	@Override
	public CollectionChecks<E> hasLength(Consumer<IntChecks> lengthChecks) {
		isNotNull();
		lengthChecks.accept(new IntChecks(value.size(), "Length of " + name));
		return this;
	}
	
	@Override
	public CollectionChecks<E> isNotEmpty() throws InvalidCollectionException {
		isNotNull();
		if (value.isEmpty()) {
			throw new InvalidCollectionException(name + " must not be empty");
		}
		return this;
	}
	
	@Override
	protected CollectionChecks<E> self() {
		return this;
	}
	
	@Override
	protected String getDefaultName() {
		return "collection";
	}
}
