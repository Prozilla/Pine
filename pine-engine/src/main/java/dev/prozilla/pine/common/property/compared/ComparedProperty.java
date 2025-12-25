package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Comparator;

/**
 * A property whose value is based on the comparison of the values of two properties.
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
	 * Checks if the value of the first property is greater than the value of the second property.
	 * @return {@code true} if the value of the first property is greater than the value of the second property.
	 * @see #isStrictlyPositive()
	 */
	public boolean isGreater() {
		return isStrictlyPositive();
	}
	
	/**
	 * Checks if the value of the first property is less than the value of the second property.
	 * @return {@code true} if the value of the first property is less than the value of the second property.
	 * @see #isStrictlyNegative()
	 */
	public boolean isLess() {
		return isStrictlyNegative();
	}
	
	/**
	 * Checks if the value of the first property is greater than or equal to the value of the second property.
	 * @return {@code true} if the value of the first property is greater than or equal to the value of the second property.
	 * @see #isPositive()
	 */
	public boolean isGreaterOrEqual() {
		return isPositive();
	}
	
	/**
	 * Checks if the value of the first property is less than or equal to the value of the second property.
	 * @return {@code true} if the value of the first property is less than or equal to the value of the second property.
	 * @see #isNegative()
	 */
	public boolean isLessOrEqual() {
		return isNegative();
	}
	
	public BooleanProperty isGreaterProperty() {
		return isStrictlyPositiveProperty();
	}
	
	public BooleanProperty isLessProperty() {
		return isStrictlyNegativeProperty();
	}
	
	public BooleanProperty isGreaterOrEqualProperty() {
		return isPositiveProperty();
	}
	
	public BooleanProperty isLessOrEqualProperty() {
		return isNegativeProperty();
	}
	
}
