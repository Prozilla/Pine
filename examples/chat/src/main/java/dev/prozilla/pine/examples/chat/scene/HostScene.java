package dev.prozilla.pine.examples.chat.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.examples.chat.Chat;
import dev.prozilla.pine.examples.chat.entity.ButtonPrefab;

public class HostScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(Anchor.CENTER);
		layoutPrefab.setDirection(Direction.RIGHT);
		layoutPrefab.setGap(new Dimension(8));
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextInputPrefab inputPrefab = new TextInputPrefab(String.valueOf(Chat.DEFAULT_PORT));
		inputPrefab.setFont(font);
		inputPrefab.setType(TextInputNode.Type.NUMBER);
		TextNode portNode = layoutNode.addChild(inputPrefab).getComponent(TextNode.class);
		
		TextInputPrefab UsernameInputPrefab = new TextInputPrefab("Anonymous");
		UsernameInputPrefab.setFont(font);
		TextNode usernameNode = layoutNode.addChild(UsernameInputPrefab).getComponent(TextNode.class);
		
		ButtonPrefab buttonPrefab = new ButtonPrefab("Start");
		buttonPrefab.setFont(font);
		buttonPrefab.setClickCallback((button) -> {
			chatApp.loadChatScene(usernameNode.text).startHost(Integer.parseInt(portNode.text));
		});
		layoutNode.addChild(buttonPrefab);
	}
	
}
