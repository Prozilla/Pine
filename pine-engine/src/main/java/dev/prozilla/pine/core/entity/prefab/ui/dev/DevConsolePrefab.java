package dev.prozilla.pine.core.entity.prefab.ui.dev;

import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.dev.DevConsole;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;

public class DevConsolePrefab extends LayoutPrefab {
	
	public DevConsolePrefab() {
		setName("DevConsole");
		setTag("dev-console");
		
		TextInputPrefab inputPrefab = new TextInputPrefab();
		inputPrefab.setAutoFocus(true);
		LayoutPrefab logsPrefab = new LayoutPrefab();
		logsPrefab.setDirection(Direction.DOWN);
		addChildren(inputPrefab, logsPrefab);
		
		setStyleSheet("style/dev-console.css");
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextInputNode textNode = entity.getFirstChild().getComponent(TextInputNode.class);
		Node inputNode = entity.getFirstChild().getComponent(Node.class);
		LayoutNode logsNode = entity.getLastChild().getComponent(LayoutNode.class);
		entity.addComponent(new DevConsole(textNode, inputNode, logsNode));
	}
}
