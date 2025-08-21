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

import java.util.ArrayList;
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
		
		addCommand(DevConsoleCommand.HELP);
		addCommand(new ConfigCommand());
		addCommand(new EnvironmentCommand("exit", ApplicationProvider::stopApplication));
		addCommand(new PrintableCommand("window", this::getWindow));
		addCommand(new LoggingCommand("assetpools", AssetPools::printInfo));
		addCommand(new LoggingCommand("system", Pine::print));
	}
	
	public void addCommand(DevConsoleCommand command) {
		commands.add(command);
	}
	
	public void addLog(String text) {
		logPrefab.setText(text);
		logsNode.getEntity().addChild(logPrefab);
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
			if (command.name.equals(args[0])) {
				return command.execute(args, this);
			}
		}

		return String.format("Command not found: %s", args[0]);
	}
	
}
