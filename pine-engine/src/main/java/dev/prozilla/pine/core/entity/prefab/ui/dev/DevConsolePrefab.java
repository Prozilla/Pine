package dev.prozilla.pine.core.entity.prefab.ui.dev;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.ui.LayoutNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.dev.DevConsoleData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;

public class DevConsolePrefab extends LayoutPrefab {
	
	public DevConsolePrefab() {
		setName("DevConsole");
		setSize(new DualDimension(Dimension.subtract(Dimension.parentSize(), new Dimension(32)), Dimension.subtract(Dimension.parentSize(), new Dimension(32))));
		setBackgroundColor(Color.black().setAlpha(0.85f));
		setDirection(Direction.UP);
		setMargin(new DualDimension(16));
		
		TextInputPrefab inputPrefab = new TextInputPrefab();
		inputPrefab.setColor(Color.white());
		inputPrefab.setBackgroundColor(Color.black().setAlpha(0.5f));
		inputPrefab.setSize(new DualDimension(Dimension.parentSize(), new Dimension(24)));
		
		LayoutPrefab logsPrefab = new LayoutPrefab();
		logsPrefab.setDirection(Direction.DOWN);
		
		addChildren(inputPrefab, logsPrefab);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextInputNode textNode = entity.getFirstChild().getComponent(TextInputNode.class);
		Node inputNode = entity.getFirstChild().getComponent(Node.class);
		LayoutNode logsNode = entity.getLastChild().getComponent(LayoutNode.class);
		entity.addComponent(new DevConsoleData(textNode, inputNode, logsNode));
	}
}
