package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;

import java.util.Arrays;

public abstract class DevConsoleCommand {
	
	public final String name;
	public final String[] aliases;
	
	public DevConsoleCommand(String name, String[] aliases) {
		this.name = Checks.isNotNull(name, "name");
		this.aliases = aliases;
	}
	
	public abstract String execute(Execution execution);
	
	public static String formatError(String commandName, String message) {
		return String.format("%s: %s", commandName, message);
	}
	
	public static class Execution implements ApplicationProvider {
		
		private final String[] arguments;
		private final DevConsoleData console;
		
		public Execution(String[] arguments, DevConsoleData console) {
			this.arguments = arguments;
			this.console = console;
		}
		
		public String getCommandName() {
			return arguments[0];
		}
		
		public String getFirstArgument() {
			return getArgument(0);
		}
		
		public String getLastArgument() {
			return getArgument(arguments.length - 2);
		}
		
		public String getArgument(int index) {
			if (index + 1 >= arguments.length) {
				return null;
			}
			return arguments[index + 1];
		}
		
		public int getArgumentCount() {
			return arguments.length - 1;
		}
		
		public String[] getArguments() {
			return Arrays.copyOfRange(this.arguments, 1, this.arguments.length);
		}
		
		@Override
		public Application getApplication() {
			return console.getApplication();
		}
		
		public DevConsoleData getConsole() {
			return console;
		}
		
		public String reject(String message) {
			return formatError(getCommandName(), message);
		}
		
	}
	
}
