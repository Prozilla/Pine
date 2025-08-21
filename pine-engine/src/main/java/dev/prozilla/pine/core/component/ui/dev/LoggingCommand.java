package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.function.Consumer;

public class LoggingCommand extends DevConsoleCommand {
	
	private final Consumer<Logger> consumer;
	
	public LoggingCommand(String name, Consumer<Logger> consumer) {
		super(name);
		this.consumer = Checks.isNotNull(consumer, "consumer");
	}
	
	@Override
	public String execute(String[] args, DevConsoleData env) {
		consumer.accept(env.logger);
		return null;
	}
	
}
