package dev.prozilla.pine.common.property;

import java.util.function.Supplier;

public class SuppliedProperty<T> extends VariableProperty<T> {
	
	private final Supplier<T> supplier;
	
	public SuppliedProperty(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public T getValue() {
		return supplier.get();
	}
	
}
