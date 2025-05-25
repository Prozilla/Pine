package dev.prozilla.pine.examples.chat.client.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.client.Client;
import dev.prozilla.pine.examples.chat.client.ClientApp;

public class ConnectScene extends Scene {
	
	private ClientApp clientApp;
	
	@Override
	public void setApplication(Application application) {
		super.setApplication(application);
		clientApp = (ClientApp)application;
	}
	
	@Override
	protected void load() {
		super.load();
		
		Entity nodeRoot = world.addEntity(new NodeRootPrefab());
		
		LayoutPrefab layoutPrefab = new LayoutPrefab();
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		layoutPrefab.setDirection(Direction.RIGHT);
		layoutPrefab.setGap(new Dimension(8));
		Entity layoutNode = nodeRoot.addChild(layoutPrefab);
		
		TextInputPrefab HostInputPrefab = new TextInputPrefab(Client.DEFAULT_HOST);
		TextNode hostNode = layoutNode.addChild(HostInputPrefab).getComponent(TextNode.class);
		
		TextInputPrefab portInputPrefab = new TextInputPrefab(String.valueOf(Client.DEFAULT_PORT));
		portInputPrefab.setType(TextInputNode.Type.NUMBER);
		TextNode portNode = layoutNode.addChild(portInputPrefab).getComponent(TextNode.class);
		
		TextInputPrefab UsernameInputPrefab = new TextInputPrefab("Anonymous");
		TextNode usernameNode = layoutNode.addChild(UsernameInputPrefab).getComponent(TextNode.class);
		
		TextButtonPrefab buttonPrefab = new TextButtonPrefab("Connect");
		buttonPrefab.setClickCallback((button) -> {
			clientApp.startClient(hostNode.text, Integer.parseInt(portNode.text), usernameNode.text);
		});
		layoutNode.addChild(buttonPrefab);
	}
	
}
