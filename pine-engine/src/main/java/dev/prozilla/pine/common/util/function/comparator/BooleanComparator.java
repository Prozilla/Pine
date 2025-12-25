package dev.prozilla.pine.common.util.function.comparator;

import dev.prozilla.pine.common.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface BooleanComparator extends Comparator<Boolean> {
	
	@Override
	default int compare(Boolean a, Boolean b) {
		return compare(ObjectUtils.unbox(a), ObjectUtils.unbox(b));
	}
	
	/**
	 * Compares two booleans.
	 * @param a The first boolean
	 * @param b The second boolean
	 * @return A negative integer, zero, or a positive integer as the first boolean is less than, equal to, or greater than the second boolean.
	 */
	int compare(boolean a, boolean b);
	
	@Override
	@NotNull
	default BooleanComparator reversed() {
		return (a, b) -> compare(b, a);
	}
	
	static BooleanComparator naturalOrder() {
		return Boolean::compare;
	}
	
}
