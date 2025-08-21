package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.core.state.config.ConfigKey;

public class ConfigCommand extends MultiCommand {
	
	public ConfigCommand() {
		super("config",
			new ReadCommand("get"),
			new ListCommand("list")
		);
	}
	
	public static class ReadCommand extends DevConsoleCommand {
		
		public ReadCommand(String name) {
			super(name);
		}
		
		@Override
		public String execute(String[] args, DevConsoleData env) {
			if (args.length < 2) {
				return String.format("%s: Missing argument", args[0]);
			}
			
			return env.getConfig().getOption(args[1]);
		}
		
	}
	
	public static class ListCommand extends DevConsoleCommand {
		
		public ListCommand(String name) {
			super(name);
		}
		
		@Override
		public String execute(String[] args, DevConsoleData env) {
			for (ConfigKey<?> key : env.getConfig().getKeys()) {
				Object value = env.getConfig().getOption(key);
				env.addLog(String.format("%s = %s", key.key(), value));
			}
			return null;
		}
		
	}
	
	
}
