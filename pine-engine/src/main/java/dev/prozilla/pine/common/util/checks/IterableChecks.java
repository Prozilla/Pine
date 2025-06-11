package dev.prozilla.pine.common.util.checks;

import java.util.function.Consumer;

public interface IterableChecks<C> {
	
	/**
	 * Performs checks on the length of the iterable.
	 * @param lengthChecks The checks to perform
	 */
	C hasLength(Consumer<IntChecks> lengthChecks);
	
	IntChecks hasLength();
	
	/**
	 * Checks that the iterable is not empty.
	 */
	C isNotEmpty();
	
}
