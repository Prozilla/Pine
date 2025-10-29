package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Supplier;

public class PrintableCommand extends DevConsoleCommand {
	
	private final Supplier<Printable> supplier;
	
	public PrintableCommand(String name, Supplier<Printable> supplier) {
		super(name);
		this.supplier = Checks.isNotNull(supplier, "supplier");
	}
	
	@Override
	String execute(String[] args, DevConsoleData env) {
		supplier.get().print(env.getLogger());
		return null;
	}
	
}
