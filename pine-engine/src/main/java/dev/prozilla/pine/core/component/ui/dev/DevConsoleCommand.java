package dev.prozilla.pine.core.component.ui.dev;

public abstract class DevConsoleCommand {
	
	public final String name;
	
	public static final DevConsoleCommand HELP = new DevConsoleCommand("help") {
		@Override
		public String execute(String[] args, DevConsoleData env) {
			for (DevConsoleCommand command : env.commands) {
				env.addLog(command.name);
			}
			return null;
		}
	};
	public static final DevConsoleCommand CLEAR = new DevConsoleCommand("clear") {
		@Override
		public String execute(String[] args, DevConsoleData env) {
			env.clearLogs();
			return null;
		}
	};
	
	public DevConsoleCommand(String name) {
		this.name = name;
	}
	
	public abstract String execute(String[] args, DevConsoleData env);
	
}
