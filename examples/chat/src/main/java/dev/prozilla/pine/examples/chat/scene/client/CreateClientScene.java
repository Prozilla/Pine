package dev.prozilla.pine.examples.chat.scene.client;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.examples.chat.entity.ButtonPrefab;
import dev.prozilla.pine.examples.chat.net.user.Client;
import dev.prozilla.pine.examples.chat.scene.SceneBase;

public class CreateClientScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setDirection(Direction.RIGHT);
		layoutPrefab.setGap(new Dimension(8));
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextInputPrefab HostInputPrefab = new TextInputPrefab(Client.DEFAULT_HOST);
		HostInputPrefab.setFont(font);
		TextNode hostNode = layoutNode.addChild(HostInputPrefab).getComponent(TextNode.class);
		
		TextInputPrefab portInputPrefab = new TextInputPrefab(String.valueOf(Client.DEFAULT_PORT));
		portInputPrefab.setFont(font);
		portInputPrefab.setType(TextInputNode.Type.NUMBER);
		TextNode portNode = layoutNode.addChild(portInputPrefab).getComponent(TextNode.class);
		
		TextInputPrefab UsernameInputPrefab = new TextInputPrefab("Anonymous");
		UsernameInputPrefab.setFont(font);
		TextNode usernameNode = layoutNode.addChild(UsernameInputPrefab).getComponent(TextNode.class);
		
		ButtonPrefab buttonPrefab = new ButtonPrefab("Connect");
		buttonPrefab.setFont(font);
		buttonPrefab.setClickCallback((button) -> {
			chatApp.startClient(hostNode.text, Integer.parseInt(portNode.text), usernameNode.text);
		});
		layoutNode.addChild(buttonPrefab);
	}
	
}
