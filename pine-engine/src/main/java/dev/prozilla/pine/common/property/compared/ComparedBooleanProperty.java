package dev.prozilla.pine.common.property.compared;

import dev.prozilla.pine.common.property.BooleanProperty;
import dev.prozilla.pine.common.util.function.comparator.BooleanComparator;
import org.jetbrains.annotations.Contract;

public class ComparedBooleanProperty extends ComparedProperty<Boolean, BooleanComparator, BooleanProperty> {
	
	/**
	 * Creates a property that compares two values.
	 * @param a The first value to compare
	 * @param comparator The comparison function
	 * @param b The second value to compare
	 */
	public ComparedBooleanProperty(boolean a, BooleanComparator comparator, boolean b) {
		this(BooleanProperty.fromValue(a), comparator, BooleanProperty.fromValue(b));
	}
	
	/**
	 * Creates a property that compares two other properties.
	 * @param a The first property to compare
	 * @param comparator The comparison function
	 * @param b The second property to compare
	 */
	public ComparedBooleanProperty(BooleanProperty a, BooleanComparator comparator, BooleanProperty b) {
		super(a, comparator, b);
	}
	
	@Override
	public int get() {
		return comparator.compare(a.get(), b.get());
	}
	
	@Override
	@Contract("-> new")
	public ComparedBooleanProperty invert() {
		return new ComparedBooleanProperty(b, comparator, a);
	}
	
	@Override
	@Contract("-> new")
	public ComparedBooleanProperty reverse() {
		return new ComparedBooleanProperty(a, comparator.reversed(), b);
	}

}
