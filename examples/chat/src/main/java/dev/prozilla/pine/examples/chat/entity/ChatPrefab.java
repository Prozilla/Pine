package dev.prozilla.pine.examples.chat.entity;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.math.vector.EdgeAlignment;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextButtonPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.chat.client.Client;

public class ChatPrefab extends LayoutPrefab {
	
	private final Client client;
	
	public ChatPrefab(Client client) {
		this.client = client;
		
		setGap(new Dimension(8));
		setDirection(Direction.DOWN);
		setAnchor(GridAlignment.CENTER);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		LayoutPrefab messageListPrefab = new LayoutPrefab();
		messageListPrefab.setGap(new Dimension(4));
		messageListPrefab.setDirection(Direction.DOWN);
		messageListPrefab.setAlignment(EdgeAlignment.START);
		Entity messageList = entity.addChild(messageListPrefab);
		
		LayoutPrefab inputBoxPrefab = new LayoutPrefab();
		inputBoxPrefab.setDirection(Direction.RIGHT);
		inputBoxPrefab.setGap(new Dimension(8));
		Entity inputBox = entity.addChild(inputBoxPrefab);
		
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
