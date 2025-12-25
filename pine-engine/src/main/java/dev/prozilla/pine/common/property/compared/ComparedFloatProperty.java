package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.util.function.comparator.FloatComparator;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public class ComparedFloatProperty extends ComparedProperty<Float, FloatComparator, FloatProperty> {
	
	/**
	 * Creates a property that compares two values.
	 * @param a The first value to compare
	 * @param comparator The comparison function
	 * @param b The second value to compare
	 */
	public ComparedFloatProperty(float a, FloatComparator comparator, float b) {
		this(new FixedFloatProperty(a), comparator, new FixedFloatProperty(b));
	}
	
	/**
	 * Creates a property that compares two other properties.
	 * @param a The first property to compare
	 * @param comparator The comparison function
	 * @param b The second property to compare
	 */
	public ComparedFloatProperty(FloatProperty a, FloatComparator comparator, FloatProperty b) {
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
	public ComparedFloatProperty invert() {
		return new ComparedFloatProperty(b, comparator, a);
	}
	
	/**
	 * Returns a compared property with the reverse of the comparator of this property.
	 * @return The reverse of this compared property.
	 * @see Comparator#reversed()
	 */
	@Contract("-> new")
	public ComparedFloatProperty reverse() {
		return new ComparedFloatProperty(a, comparator.reversed(), b);
	}

}
