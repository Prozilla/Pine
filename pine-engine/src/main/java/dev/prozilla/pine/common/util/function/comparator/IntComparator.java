package dev.prozilla.pine.common.util.function.comparator;

import dev.prozilla.pine.common.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface IntComparator extends Comparator<Integer> {
	
	@Override
	default int compare(Integer a, Integer b) {
		return compare(ObjectUtils.unbox(a), ObjectUtils.unbox(b));
	}
	
	/**
	 * Compares two integers.
	 * @param a The first integer
	 * @param b The second integer
	 * @return A negative integer, zero, or a positive integer as the first integer is less than, equal to, or greater than the second integer.
	 */
	int compare(int a, int b);
	
	@Override
	@NotNull
	default IntComparator reversed() {
		return (a, b) -> compare(b, a);
	}
	
	static IntComparator naturalOrder() {
		return Integer::compare;
	}
	
}
