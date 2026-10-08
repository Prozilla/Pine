package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.function.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DevCommandBuilder {
	
	protected String name;
	protected final List<String> aliases;
	protected Function<DevConsoleCommand.Execution, String> execute;
	protected final List<DevCommandBuilder> subCommandBuilders;
	
	public DevCommandBuilder(String name) {
		this.name = Checks.isNotNull(name, "name").toLowerCase();
		
		aliases = new ArrayList<>();
		subCommandBuilders = new ArrayList<>();
	}
	
	public DevCommandBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public DevCommandBuilder addAlias(String alias) {
		aliases.add(Checks.isNotNull(alias).toLowerCase());
		return this;
	}
	
	public DevCommandBuilder setExecute(Supplier<Printable> supplier) {
		return setExecute((execution) -> {
			return String.valueOf(supplier.get());
		});
	}
	
	public DevCommandBuilder setExecute(Consumer<DevConsoleCommand.Execution> execute) {
		return setExecute((execution) -> {
			execute.accept(execution);
			return null;
		});
	}
	
	public DevCommandBuilder setExecute(Callback callback) {
		return setExecute((execution) -> {
			callback.run();
		});
	}
	
	public DevCommandBuilder setExecute(Function<DevConsoleCommand.Execution, String> execute) {
		this.execute = execute;
		return this;
	}
	
	public DevCommandBuilder addSimpleSubCommand(String name, Consumer<DevConsoleCommand.Execution> execute) {
		return addSubCommand(name, (subCommand) -> subCommand.setExecute(execute));
	}
	
	public DevCommandBuilder addSimpleSubCommand(String name, Function<DevConsoleCommand.Execution, String> execute) {
		return addSubCommand(name, (subCommand) -> subCommand.setExecute(execute));
	}
	
	public DevCommandBuilder addSubCommand(String name, Consumer<DevCommandBuilder> builder) {
		DevCommandBuilder subCommandBuilder = new DevCommandBuilder(name);
		builder.accept(subCommandBuilder);
		subCommandBuilders.add(subCommandBuilder);
		return this;
	}
	
	public DevConsoleCommand build() {
		if (!subCommandBuilders.isEmpty()) {
			DevConsoleCommand[] subCommands = new DevConsoleCommand[subCommandBuilders.size()];
			for (int i = 0; i < subCommands.length; i++) {
				subCommands[i] = subCommandBuilders.get(i).build();
			}
			return new MultiCommand(name, aliases.toArray(new String[0]), subCommands);
		}
		
		return new DevConsoleCommand(name, aliases.toArray(new String[0])) {
			@Override
			public String execute(Execution execution) {
				return execute.apply(execution);
			}
		};
	}
	
}
