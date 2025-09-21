package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Supplier;

/**
 * A property whose value is determined by a {@link Supplier} function.
 */
public class SuppliedProperty<T> extends VariableProperty<T> {
	
	private final Supplier<T> supplier;
	
	/**
	 * Creates a property whose value is retrieved from another property.
	 */
	public SuppliedProperty(VariableProperty<T> supplier) {
		this(supplier::getValue);
	}
	
	public SuppliedProperty(Supplier<T> supplier) {
		this.supplier = Checks.isNotNull(supplier, "supplier");
	}
	
	@Override
	public T getValue() {
		return supplier.get();
	}
	
}
