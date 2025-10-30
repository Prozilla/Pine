package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class MultiCommand extends DevConsoleCommand {
	
	private final List<DevConsoleCommand> subCommands;
	
	public MultiCommand(String name, DevConsoleCommand... subCommands) {
		this(name);
		addSubCommands(subCommands);
	}
	
	public MultiCommand(String name) {
		super(name);
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
	public String execute(String[] args, DevConsoleData env) {
		if (args.length < 2) {
			return String.format("%s: Missing subcommand", args[0]);
		}
		
		String subCommandName = args[1];
		
		for (DevConsoleCommand subCommand : subCommands) {
			if (subCommand.name.equals(subCommandName)) {
				return subCommand.execute(ArrayUtils.removeFirst(args), env);
			}
		}
		
		return String.format("%s: Invalid subcommand: %s", args[0], subCommandName);
	}
	
}
