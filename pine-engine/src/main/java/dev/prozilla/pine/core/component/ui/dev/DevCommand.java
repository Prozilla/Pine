package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;

import java.util.Arrays;

public abstract class DevCommand {
	
	public final String name;
	public final String[] aliases;
	
	public DevCommand(String name, String[] aliases) {
		this.name = Checks.isNotNull(name, "name");
		this.aliases = aliases;
	}
	
	public boolean matches(String name) {
		return this.name.equals(name) || (aliases != null && ArrayUtils.contains(aliases, name));
	}
	
	public abstract void execute(Context context);
	
	public static String formatError(String commandName, String message) {
		return String.format("%s: %s", commandName, message);
	}
	
	public static class Context implements ApplicationProvider {
		
		private final String[] input;
		private final DevConsole console;
		
		public Context(String[] input, DevConsole console) {
			this.input = input;
			this.console = console;
		}
		
		public String getCommandName() {
			return input[0];
		}
		
		public String getFirstArgument() {
			return getArgument(0);
		}
		
		public String getLastArgument() {
			return getArgument(input.length - 2);
		}
		
		public String getArgument(int index) {
			if (index + 1 >= input.length) {
				return null;
			}
			return input[index + 1];
		}
		
		public int getArgumentCount() {
			return input.length - 1;
		}
		
		public String[] getArguments() {
			return Arrays.copyOfRange(this.input, 1, this.input.length);
		}
		
		@Override
		public Application getApplication() {
			return console.getApplication();
		}
		
		public DevConsole getConsole() {
			return console;
		}
		
		public String fail(String message) {
			return formatError(getCommandName(), message);
		}
		
		public Context fork() {
			return new Context(getArguments(), console);
		}
		
	}
	
}
