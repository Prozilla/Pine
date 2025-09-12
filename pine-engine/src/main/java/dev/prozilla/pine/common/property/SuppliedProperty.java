package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Supplier;

public class SuppliedProperty<T> extends VariableProperty<T> {
	
	private final Supplier<T> supplier;
	
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
