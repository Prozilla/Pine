package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.fixed.FixedObjectProperty;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

public class ComparedObjectProperty<T> extends ComparedProperty<T, Comparator<T>, Property<T>> {
	
	/**
	 * Creates a property that compares two values.
	 * @param a The first value to compare
	 * @param comparator The comparison function
	 * @param b The second value to compare
	 */
	public ComparedObjectProperty(T a, Comparator<T> comparator, T b) {
		this(new FixedObjectProperty<>(a), comparator, new FixedObjectProperty<>(b));
	}
	
	/**
	 * Creates a property that compares two other properties.
	 * @param a The first property to compare
	 * @param comparator The comparison function
	 * @param b The second property to compare
	 */
	public ComparedObjectProperty(Property<T> a, Comparator<T> comparator, Property<T> b) {
		super(a, comparator, b);
	}
	
	@Override
	@Contract("-> new")
	public ComparedObjectProperty<T> invert() {
		return new ComparedObjectProperty<>(b, comparator, a);
	}
	
	@Override
	@Contract("-> new")
	public ComparedObjectProperty<T> reverse() {
		return new ComparedObjectProperty<>(a, comparator.reversed(), b);
	}

}
