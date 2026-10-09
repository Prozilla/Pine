package dev.prozilla.pine.core.component.ui.dev;

import java.util.ArrayList;
import java.util.List;

public class MultiCommand extends DevCommand {
	
	private final List<DevCommand> subCommands;
	
	public MultiCommand(String name, String[] aliases, DevCommand... subCommands) {
		this(name, aliases);
		addSubCommands(subCommands);
	}
	
	public MultiCommand(String name, String[] aliases) {
		super(name, aliases);
		subCommands = new ArrayList<>();
	}
	
	public void addSubCommands(DevCommand... subCommands) {
		for (DevCommand subCommand : subCommands) {
			addSubCommand(subCommand);
		}
	}
	
	public void addSubCommand(DevCommand subCommand) {
		subCommands.add(subCommand);
	}
	
	@Override
	public void execute(Context context) {
		if (context.getArgumentCount() < 1) {
			context.fail("Missing subcommand");
			return;
		}
		
		String subCommandName = context.getFirstArgument();
		
		for (DevCommand subCommand : subCommands) {
			if (subCommand.name.equals(subCommandName)) {
				subCommand.execute(context.fork());
				return;
			}
		}
		
		context.getConsole().log(String.format("%s: Invalid subcommand: %s", context.getCommandName(), subCommandName));
	}
	
}
