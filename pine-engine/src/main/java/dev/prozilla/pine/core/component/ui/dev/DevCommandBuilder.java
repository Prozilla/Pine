package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.function.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DevCommandBuilder {
	
	protected String name;
	protected final List<String> aliases;
	protected Consumer<DevCommand.Context> executor;
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
	
	public DevCommandBuilder setGenerator(Supplier<Printable> generator) {
		return setExecutor((context) -> {
			context.getConsole().log(String.valueOf(generator.get()));
		});
	}
	
	public DevCommandBuilder setCallback(Callback callback) {
		return setExecutor((context) -> {
			callback.run();
		});
	}
	
	public DevCommandBuilder setExecutor(Consumer<DevCommand.Context> executor) {
		this.executor = executor;
		return this;
	}
	
	public DevCommandBuilder addSimpleSubCommand(String name, Consumer<DevCommand.Context> execute) {
		return addSubCommand(name, (subCommand) -> subCommand.setExecutor(execute));
	}
	
	public DevCommandBuilder addSubCommand(String name, Consumer<DevCommandBuilder> builder) {
		DevCommandBuilder subCommandBuilder = new DevCommandBuilder(name);
		builder.accept(subCommandBuilder);
		subCommandBuilders.add(subCommandBuilder);
		return this;
	}
	
	public DevCommand build() {
		if (!subCommandBuilders.isEmpty()) {
			DevCommand[] subCommands = new DevCommand[subCommandBuilders.size()];
			for (int i = 0; i < subCommands.length; i++) {
				subCommands[i] = subCommandBuilders.get(i).build();
			}
			return new MultiCommand(name, aliases.toArray(new String[0]), subCommands);
		}
		
		return new DevCommand(name, aliases.toArray(new String[0])) {
			@Override
			public void execute(Context context) {
				executor.accept(context);
			}
		};
	}
	
}
