package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Consumer;

public class EnvironmentCommand extends DevConsoleCommand {
	
	private final Consumer<DevConsoleData> consumer;
	
	public EnvironmentCommand(String name, Consumer<DevConsoleData> consumer) {
		super(name);
		this.consumer = Checks.isNotNull(consumer, "consumer");
	}
	
	@Override
	public String execute(String[] args, DevConsoleData env) {
		consumer.accept(env);
		return null;
	}
	
}
