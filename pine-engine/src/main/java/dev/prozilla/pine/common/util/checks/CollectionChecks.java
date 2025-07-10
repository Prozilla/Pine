package dev.prozilla.pine.common.util.checks;

import dev.prozilla.pine.common.exception.InvalidCollectionException;
import dev.prozilla.pine.common.exception.InvalidObjectException;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Utility class for performing checks on collections.
 */
public final class CollectionChecks<E> extends ChecksBase<Collection<E>, CollectionChecks<E>> implements IterableChecks<CollectionChecks<E>> {
	
	public CollectionChecks(Collection<E> value) {
		this(value, null);
	}
	
	public CollectionChecks(Collection<E> value, String name) {
		super(value, name);
	}
	
	@Override
	public CollectionChecks<E> hasLength(Consumer<IntChecks> lengthChecks) throws InvalidObjectException {
		lengthChecks.accept(hasLength());
		return this;
	}
	
	@Override
	public IntChecks hasLength() throws InvalidObjectException {
		isNotNull();
		return new IntChecks(value.size(), "length of " + name);
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
