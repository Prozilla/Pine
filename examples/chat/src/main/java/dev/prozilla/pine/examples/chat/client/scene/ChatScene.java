package dev.prozilla.pine.examples.chat.client.scene;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.*;
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
		layoutPrefab.setGap(new Dimension(8));
		layoutPrefab.setDirection(Direction.DOWN);
		layoutPrefab.setAnchor(GridAlignment.CENTER);
		Entity layout = nodeRoot.addChild(layoutPrefab);
		
		LayoutPrefab messageListPrefab = new LayoutPrefab();
		messageListPrefab.setGap(new Dimension(4));
		messageListPrefab.setDirection(Direction.DOWN);
		messageListPrefab.setAlignment(EdgeAlignment.START);
		Entity messageList = layout.addChild(messageListPrefab);
		
		LayoutPrefab inputBoxPrefab = new LayoutPrefab();
		inputBoxPrefab.setDirection(Direction.RIGHT);
		inputBoxPrefab.setGap(new Dimension(8));
		Entity inputBox = layout.addChild(inputBoxPrefab);
		
		TextInputPrefab messageInputPrefab = new TextInputPrefab();
		messageInputPrefab.setSize(new DualDimension(128, 24));
		TextNode messageNode = inputBox.addChild(messageInputPrefab).getComponent(TextNode.class);
		
		TextButtonPrefab sendButtonPrefab = new TextButtonPrefab("Send");
		sendButtonPrefab.setClickCallback((button) -> {
			client.sendMessage(messageNode.text);
			messageNode.setText("");
		});
		inputBox.addChild(sendButtonPrefab);
		
		TextPrefab messagePrefab = new TextPrefab();
		
		client.addMessageListener((message) -> {
			messagePrefab.setText(Ansi.strip(message));
			messageList.addChild(messagePrefab);
		});
	}
}
