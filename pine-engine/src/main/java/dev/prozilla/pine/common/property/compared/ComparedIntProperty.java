package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.fixed.FixedIntProperty;
import dev.prozilla.pine.common.util.function.comparator.IntComparator;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public class ComparedIntProperty extends ComparedProperty<Integer, IntComparator, IntProperty> {
	
	/**
	 * Creates a property that compares two values.
	 * @param a The first value to compare
	 * @param comparator The comparison function
	 * @param b The second value to compare
	 */
	public ComparedIntProperty(int a, IntComparator comparator, int b) {
		this(new FixedIntProperty(a), comparator, new FixedIntProperty(b));
	}
	
	/**
	 * Creates a property that compares two other properties.
	 * @param a The first property to compare
	 * @param comparator The comparison function
	 * @param b The second property to compare
	 */
	public ComparedIntProperty(IntProperty a, IntComparator comparator, IntProperty b) {
		super(a, comparator, b);
	}
	
	@Override
	public int get() {
		return comparator.compare(a.get(), b.get());
	}
	
	/**
	 * Returns the inverse of this compared property with the same comparator, by swapping the two properties.
	 * @return The inverse of this compared property.
	 */
	@Contract("-> new")
	public ComparedIntProperty invert() {
		return new ComparedIntProperty(b, comparator, a);
	}
	
	/**
	 * Returns a compared property with the reverse of the comparator of this property.
	 * @return The reverse of this compared property.
	 * @see Comparator#reversed()
	 */
	@Contract("-> new")
	public ComparedIntProperty reverse() {
		return new ComparedIntProperty(a, comparator.reversed(), b);
	}

}
