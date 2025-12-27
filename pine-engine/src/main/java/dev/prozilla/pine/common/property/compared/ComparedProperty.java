package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Comparator;

/**
 * A property whose value is based on the comparison of the values of two properties.
 *
 * <p>A negative integer, zero or a positive integer respectively means that the value of the first property is less than, equal to, or greater than the value of the second property.</p>
 * @param <C> The type of comparator
 * @param <P> The type of properties
 */
public abstract class ComparedProperty<T, C extends Comparator<T>, P extends Property<T>> implements IntProperty {
	
	protected final C comparator;
	protected final P a;
	protected final P b;
	
	/**
	 * Creates a property that compares two other properties.
	 * @param a The first property to compare
	 * @param comparator The comparison function
	 * @param b The second property to compare
	 */
	public ComparedProperty(P a, C comparator, P b) {
		this.comparator = Checks.isNotNull(comparator, "comparator");
		this.a = Checks.isNotNull(a, "a");
		this.b = Checks.isNotNull(b, "b");
	}
	
	/**
	 * Compares the values of the two properties.
	 * @return A negative integer, zero, or a positive integer as the value of the first property is less than, equal to, or greater than the value of the second property.
	 */
	@Override
	public int get() {
		return comparator.compare(a.getValue(), b.getValue());
	}
	
	/**
	 * Returns the inverse of this compared property with the same comparator, by swapping the two properties.
	 * @return The inverse of this compared property.
	 */
	abstract public ComparedProperty<T, C, P> invert();
	
	/**
	 * Returns a compared property with the reverse of the comparator of this property.
	 * @return The reverse of this compared property.
	 * @see Comparator#reversed()
	 */
	abstract public ComparedProperty<T, C, P> reverse();
	
}
