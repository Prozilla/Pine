package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.property.selection.SingleSelectionProperty;
import dev.prozilla.pine.common.system.Color;
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

public class DevConsole extends Component {
	
	public final TextInputNode textNode;
	public final Node inputNode;
	public final LayoutNode logsNode;
	public final TextPrefab logPrefab;
	public final Logger logger;
	public final SingleSelectionProperty<String> history;
	public final List<DevCommand> commands;
	
	public DevConsole(TextInputNode textNode, Node inputNode, LayoutNode logsNode) {
		this.textNode = textNode;
		this.inputNode = inputNode;
		this.logsNode = logsNode;
		
		logPrefab = new TextPrefab();
		logPrefab.setColor(Color.white());
		
		logger = new Logger(this::log, this::log);
		history = new SingleSelectionProperty<>();
		commands = new ArrayList<>();
		
		addDefaultCommands();
	}
	
	private void addDefaultCommands() {
		addCommand(new DevCommandBuilder("help").setExecutor((context) -> {
			for (DevCommand command : context.getConsole().commands) {
				StringBuilder stringBuilder = new StringBuilder(command.name);
				if (command.aliases != null && command.aliases.length > 0) {
					stringBuilder.append(" ");
					stringBuilder.append(Arrays.toString(command.aliases));
				}
				context.getConsole().log(stringBuilder.toString());
			}
		}));
		addCommand(new DevCommandBuilder("clear").setExecutor((context) -> {
			context.getConsole().clearLogs();
		}));
		addCommand(new DevCommandBuilder("config")
			.addSimpleSubCommand("read", (context) -> {
				if (context.getArgumentCount() < 1) {
					context.fail("Missing argument");
					return;
				}
				
				context.getConfig().getOption(context.getFirstArgument());
			})
			.addSimpleSubCommand("list", (context) -> {
			   for (ConfigKey<?> key : context.getConfig().getKeys()) {
			       Object value = context.getConfig().getOption(key);
			       context.getConsole().log(String.format("%s = %s", key.key(), value));
			   }
			})
		);
		addCommand(new DevCommandBuilder("exit")
			.addAlias("quit")
			.addAlias("stop")
			.setExecutor(ApplicationProvider::stopApplication)
		);
		addCommand(new DevCommandBuilder("window").setGenerator(this::getWindow));
		addCommand(new DevCommandBuilder("assetpools")
			.addAlias("pools")
			.setCallback(AssetPools::printInfo)
		);
		addCommand(new DevCommandBuilder("system").setCallback(Pine::print));
		addCommand(new DevCommandBuilder("scene").setExecutor((context) -> {
			if (context.getArgumentCount() < 1) {
				context.fail("Expected 1 argument, received " + context.getArgumentCount());
				return;
			}
			context.getApplication().loadScene(Integer.parseInt(context.getFirstArgument()));
		}));
		addCommand(new DevCommandBuilder("render").setExecutor((context) -> {
			if (context.getArgumentCount() < 1) {
				context.fail("Expected 1 argument, received " + context.getArgumentCount());
				return;
			}
			
			RenderMode renderMode;
			try {
				renderMode = RenderMode.valueOf(context.getFirstArgument().toUpperCase());
			} catch (IllegalArgumentException e) {
				context.fail("Unknown render mode: " + context.getFirstArgument());
				return;
			}
			
			context.getConfig().rendering.renderMode.setValue(renderMode);
			context.getConsole().log("Set render mode to: " + renderMode.toString().toLowerCase());
		}));
	}
	
	public void addCommand(DevCommandBuilder builder) {
		addCommand(builder.build());
	}
	
	public void addCommand(DevCommand command) {
		commands.add(command);
	}
	
	public void log(String text) {
		logPrefab.setText(text);
		logsNode.getEntity().addChild(logPrefab);
	}
	
	public void clearLogs() {
		logsNode.getEntity().destroyChildren();
	}
	
	public void execute(String input) {
		if (input.isBlank()) {
			return;
		}
		
		history.addItem(input);
		history.clearSelection();
		
		String[] tokens = input.trim().split("\\s+");
		tokens[0] = tokens[0].toLowerCase();
		
		for (DevCommand command : commands) {
			if (command.matches(tokens[0])) {
				try {
					DevCommand.Context context = new DevCommand.Context(tokens, this);
					command.execute(context);
				} catch (Exception e) {
					log(DevCommand.formatError(command.name, e.getMessage()));
				}
			}
		}
		
		log(DevCommand.formatError(tokens[0], "Command not found"));
	}
	
}
