package dev.prozilla.pine.examples.chat.entity;

import dev.prozilla.pine.common.asset.text.Font;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Alignment;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Direction;
import dev.prozilla.pine.common.property.bindable.BindableStringProperty;
import dev.prozilla.pine.common.property.bindable.SimpleBindableStringProperty;
import dev.prozilla.pine.common.system.Ansi;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.LayoutPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextInputPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.examples.chat.net.user.User;

public class ChatPrefab extends LayoutPrefab {
	
	private final User user;
	private final Font font;
	
	public ChatPrefab(User user, Font font) {
		this.user = user;
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
		Entity messageList = entity.addChild(messageListPrefab);
		
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
			if (!inputProperty.isBlank()) {
				user.sendMessage(inputProperty.swapValue(""));
			}
		});
		inputBox.addChild(sendButtonPrefab);
		
		TextPrefab messagePrefab = new TextPrefab();
		messagePrefab.setFont(font);
		user.addMessageListener((event) -> {
			messagePrefab.setText(Ansi.strip(event.getTarget()));
			messageList.addChild(messagePrefab);
		});
	}
}
