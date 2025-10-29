package dev.prozilla.pine.core.component.ui.dev;

import dev.prozilla.pine.Pine;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;

public class DevConsoleData extends Component {
	
	public final TextInputNode textNode;
	public final Node inputNode;
	public final LayoutNode logsNode;
	public final TextPrefab logPrefab;
	public final Logger logger;
	
	public DevConsoleData(TextInputNode textNode, Node inputNode, LayoutNode logsNode) {
		this.textNode = textNode;
		this.inputNode = inputNode;
		this.logsNode = logsNode;
		
		logPrefab = new TextPrefab();
		logPrefab.setColor(Color.white());
		
		logger = new Logger(this::addLog, this::addLog);
	}
	
	public void addLog(String text) {
		logPrefab.setText(text);
		logsNode.getEntity().addChild(logPrefab);
	}
	
	public String handleInput(String input) {
		if (input.isBlank()) {
			return null;
		}
		
		String[] args = input.split(" ");
		String command = args[0].toLowerCase();
		
		String output = null;
		
		switch (command) {
			case "exit" -> getApplication().stop();
			case "window" -> getWindow().print(logger);
			case "system" -> Pine.print(logger);
		}
		
		return output;
	}
	
}
