package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.selection.SingleSelectionProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.common.util.function.Callback;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.rendering.RenderMode;
import dev.prozilla.pine.core.state.config.ConfigKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevConsoleData extends Component {
	
	public final TextInputNode textNode;
	public final Node inputNode;
	public final LayoutNode logsNode;
	public final TextPrefab logPrefab;
	public final Logger logger;
	public final SingleSelectionProperty<String> history;
	public final List<DevConsoleCommand> commands;
	
	public DevConsoleData(TextInputNode textNode, Node inputNode, LayoutNode logsNode) {
		this.textNode = textNode;
		this.inputNode = inputNode;
		this.logsNode = logsNode;
		
		logPrefab = new TextPrefab();
		logPrefab.setColor(Color.white());
		
		logger = new Logger(this::addLog, this::addLog);
		history = new SingleSelectionProperty<>();
		commands = new ArrayList<>();
		
		addDefaultCommands();
	}
	
	private void addDefaultCommands() {
		addCommand(new DevCommandBuilder("help").setExecute((execution) -> {
			for (DevConsoleCommand command : execution.getConsole().commands) {
				StringBuilder stringBuilder = new StringBuilder(command.name);
				if (command.aliases != null && command.aliases.length > 0) {
					stringBuilder.append(" ");
					stringBuilder.append(Arrays.toString(command.aliases));
				}
				execution.getConsole().addLog(stringBuilder.toString());
			}
		}));
		addCommand(new DevCommandBuilder("clear").setExecute((execution) -> {
			execution.getConsole().clearLogs();
		}));
		addCommand(new DevCommandBuilder("config")
			.addSimpleSubCommand("read", (execution) -> {
			   if (execution.getArgumentCount() < 1) {
			       return execution.reject("Missing argument");
			   }
			   
			   return execution.getConfig().getOption(execution.getFirstArgument());
			})
			.addSimpleSubCommand("list", (execution) -> {
			   for (ConfigKey<?> key : execution.getConfig().getKeys()) {
			       Object value = execution.getConfig().getOption(key);
			       execution.getConsole().addLog(String.format("%s = %s", key.key(), value));
			   }
			})
		);
		addCommand(new DevCommandBuilder("exit")
			.addAlias("quit")
			.addAlias("stop")
			.setExecute(ApplicationProvider::stopApplication)
		);
		addCommand(new DevCommandBuilder("window").setExecute(this::getWindow));
		addCommand(new DevCommandBuilder("assetpools")
			.addAlias("pools")
			.setExecute((Callback)AssetPools::printInfo)
		);
		addCommand(new DevCommandBuilder("window").setExecute((Callback)Pine::print));
		addCommand(new DevCommandBuilder("scene").setExecute((execution) -> {
			if (execution.getArgumentCount() < 1) {
				return execution.reject("Expected 1 argument, received " + execution.getArgumentCount());
			}
			execution.getApplication().loadScene(Integer.parseInt(execution.getFirstArgument()));
			return null;
		}));
		addCommand(new DevCommandBuilder("render").setExecute((execution) -> {
			if (execution.getArgumentCount() < 1) {
				return execution.reject("Expected 1 argument, received " + execution.getArgumentCount());
			}
			
			RenderMode renderMode;
			try {
				renderMode = RenderMode.valueOf(execution.getFirstArgument().toUpperCase());
			} catch (IllegalArgumentException e) {
				return "Unknown render mode: " + execution.getFirstArgument();
			}
			
			execution.getConfig().rendering.renderMode.setValue(renderMode);
			return "Set render mode to: " + renderMode.toString().toLowerCase();
		}));
	}
	
	public void addCommand(DevCommandBuilder builder) {
		addCommand(builder.build());
	}
	
	public void addCommand(DevConsoleCommand command) {
		commands.add(command);
	}
	
	public void addLog(String text) {
		logPrefab.setText(text);
		logsNode.getEntity().addChild(logPrefab);
	}
	
	public void clearLogs() {
		logsNode.getEntity().destroyChildren();
	}
	
	public String handleInput(String input) {
		if (input.isBlank()) {
			return null;
		}
		
		history.addItem(input);
		history.clearSelection();
		
		String[] args = input.split(" ");
		args[0] = args[0].toLowerCase();
		
		for (DevConsoleCommand command : commands) {
			if (command.name.equals(args[0]) || (command.aliases != null && ArrayUtils.contains(command.aliases, args[0]))) {
				try {
					DevConsoleCommand.Execution execution = new DevConsoleCommand.Execution(args, this);
					return command.execute(execution);
				} catch (Exception e) {
					return DevConsoleCommand.formatError(command.name, e.getMessage());
				}
			}
		}
		
		return DevConsoleCommand.formatError(args[0], "Command not found");
	}
	
}
