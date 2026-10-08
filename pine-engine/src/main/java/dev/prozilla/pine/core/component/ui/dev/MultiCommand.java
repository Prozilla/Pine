package dev.prozilla.pine.core.component.ui.dev;

import java.util.ArrayList;
import java.util.List;

public class MultiCommand extends DevConsoleCommand {
	
	private final List<DevConsoleCommand> subCommands;
	
	public MultiCommand(String name, String[] aliases, DevConsoleCommand... subCommands) {
		this(name, aliases);
		addSubCommands(subCommands);
	}
	
	public MultiCommand(String name, String[] aliases) {
		super(name, aliases);
		subCommands = new ArrayList<>();
	}
	
	public void addSubCommands(DevConsoleCommand... subCommands) {
		for (DevConsoleCommand subCommand : subCommands) {
			addSubCommand(subCommand);
		}
	}
	
	public void addSubCommand(DevConsoleCommand subCommand) {
		subCommands.add(subCommand);
	}
	
	@Override
	public String execute(Execution execution) {
		if (execution.getArgumentCount() < 1) {
			return String.format("%s: Missing subcommand", execution.getCommandName());
		}
		
		String subCommandName = execution.getFirstArgument();
		
		for (DevConsoleCommand subCommand : subCommands) {
			if (subCommand.name.equals(subCommandName)) {
				return subCommand.execute(new Execution(execution.getArguments(), execution.getConsole()));
			}
		}
		
		return String.format("%s: Invalid subcommand: %s", execution.getCommandName(), subCommandName);
	}
	
}
