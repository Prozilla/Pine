package dev.prozilla.pine.common.util.parser;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Supplier;

public abstract class SuppliedSequentialParser<T> extends SequentialParser<T> {
	
	private final Supplier<T> supplier;
	
	public SuppliedSequentialParser(Supplier<T> supplier) {
		this.supplier = Checks.isNotNull(supplier, "supplier");
	}
	
	@Override
	public boolean parse(String input) {
		return parse(input, null);
	}
	
	public boolean parse(String input, T target) {
		startStep(input, target != null ? target : supplier.get());
		return parse();
	}
	
	protected abstract boolean parse();
	
}
