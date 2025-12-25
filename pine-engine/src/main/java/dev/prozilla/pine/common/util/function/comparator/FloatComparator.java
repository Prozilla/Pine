package dev.prozilla.pine.common.util.function.comparator;

import dev.prozilla.pine.common.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface FloatComparator extends Comparator<Float> {
	
	@Override
	default int compare(Float a, Float b) {
		return compare(ObjectUtils.unbox(a), ObjectUtils.unbox(b));
	}
	
	/**
	 * Compares two floats.
	 * @param a The first float
	 * @param b The second float
	 * @return A negative integer, zero, or a positive integer as the first float is less than, equal to, or greater than the second float.
	 */
	int compare(float a, float b);
	
	@Override
	@NotNull
	default FloatComparator reversed() {
		return (a, b) -> compare(b, a);
	}
	
	static FloatComparator naturalOrder() {
		return Float::compare;
	}
	
}
