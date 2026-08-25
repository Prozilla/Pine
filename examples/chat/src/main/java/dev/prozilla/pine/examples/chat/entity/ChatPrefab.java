package dev.prozilla.pine.examples.chat.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Alignment;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.bindable.BindableStringProperty;
import dev.prozilla.pine.common.property.bindable.SimpleBindableStringProperty;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.examples.chat.EntityTag;
import dev.prozilla.pine.examples.chat.request.SendMessageRequest;
import dev.prozilla.pine.extensions.pinet.component.NetworkManager;

public class ChatPrefab extends LayoutPrefab {
	
	private final NetworkManager network;
	private final Font font;
	
	public ChatPrefab(NetworkManager network, Font font) {
		this.network = network;
		this.font = font;
		
		setGap(new Dimension(8));
		setDirection(Direction.DOWN);
		setAnchor(Anchor.CENTER);
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		LayoutPrefab messageListPrefab = new LayoutPrefab();
		messageListPrefab.setGap(new Dimension(4));
		messageListPrefab.setDirection(Direction.DOWN);
		messageListPrefab.setAlignment(Alignment.START);
		messageListPrefab.setTag(EntityTag.MESSAGE_LIST);
		entity.addChild(messageListPrefab);
		
		LayoutPrefab inputBoxPrefab = new LayoutPrefab();
		inputBoxPrefab.setDirection(Direction.RIGHT);
		inputBoxPrefab.setGap(new Dimension(8));
		Entity inputBox = entity.addChild(inputBoxPrefab);
		
		BindableStringProperty inputProperty = new SimpleBindableStringProperty("");
		TextInputPrefab messageInputPrefab = new TextInputPrefab();
		messageInputPrefab.setTextProperty(inputProperty);
		messageInputPrefab.setFont(font);
		messageInputPrefab.setSize(new DualDimension(128, 24));
		inputBox.addChild(messageInputPrefab);
		
		ButtonPrefab sendButtonPrefab = new ButtonPrefab("Send");
		sendButtonPrefab.setFont(font);
		sendButtonPrefab.setClickCallback((button) -> {
			if (!inputProperty.isBlank() && network.isConnected()) {
				network.send(new SendMessageRequest(network.getLocalClientId(), inputProperty.swapValue("")));
			}
		});
		inputBox.addChild(sendButtonPrefab);
	}
}
