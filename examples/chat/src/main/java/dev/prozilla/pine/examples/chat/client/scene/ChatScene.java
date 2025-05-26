package dev.prozilla.pine.examples.chat.client.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.chat.client.Client;

public class ChatScene extends Scene {
	
	private final Client client;
	
	public ChatScene(Client client) {
		this.client = client;
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
		
		TextInputPrefab messageInputPrefab = new TextInputPrefab();
		messageInputPrefab.setSize(new DualDimension(128, 24));
		TextNode messageNode = layoutNode.addChild(messageInputPrefab).getComponent(TextNode.class);
		
		TextButtonPrefab sendButtonPrefab = new TextButtonPrefab("Send");
		sendButtonPrefab.setClickCallback((button) -> {
			client.sendMessage(messageNode.text);
			messageNode.setText("");
		});
		layoutNode.addChild(sendButtonPrefab);
	}
}
