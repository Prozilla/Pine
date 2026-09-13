package dev.prozilla.pine.examples.sokoban.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.ui.ButtonNode;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.sokoban.GameManager;
import dev.prozilla.pine.examples.sokoban.entity.ui.ButtonPrefab;

public abstract class OnlineMenuScene extends Scene {
	
	private Entity layout;
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.setBackgroundColor(GameManager.BACKGROUND_COLOR);
		
		Entity nodeRoot = addEntity(new NodeRootPrefab());
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(Anchor.CENTER);
		layoutPrefab.setDirection(Direction.RIGHT);
		layoutPrefab.setGap(new Dimension(8));
		layout = nodeRoot.addChild(layoutPrefab);
	}
	
	protected TextNode addTextInput(String defaultText) {
		TextInputPrefab textInputPrefab = new TextInputPrefab(defaultText);
		textInputPrefab.setFont(GameManager.instance.font);
		return layout.addChild(textInputPrefab).getComponent(TextNode.class);
	}
	
	protected TextNode addPortInput(int defaultPort) {
		TextInputPrefab portInputPrefab = new TextInputPrefab(String.valueOf(defaultPort));
		portInputPrefab.setFont(GameManager.instance.font);
		portInputPrefab.setType(TextInputNode.Type.NUMBER);
		return layout.addChild(portInputPrefab).getComponent(TextNode.class);
	}
	
	protected void addButton(String text, ButtonNode.ClickCallback clickCallback) {
		ButtonPrefab buttonPrefab = new ButtonPrefab(text);
		buttonPrefab.setClickCallback(clickCallback);
		layout.addChild(buttonPrefab);
	}
	
}
