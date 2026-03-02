package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.property.observable.ObservableStringProperty;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TextInputNode;
import dev.prozilla.pine.core.component.ui.TextNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

@Components({ TextInputNode.class, TextNode.class, Node.class, Transform.class })
public class TextInputPrefab extends TextPrefab {
	
	protected TextInputNode.Type type;
	protected ObservableStringProperty textProperty;
	
	public TextInputPrefab() {
		this(null);
	}
	
	public TextInputPrefab(String value) {
		super(value);
		setName("TextInput");
		setAutoFocus(true);
		setSize(new DualDimension(64, 24));
		setHTMLTag("input");
		setTabIndex(0);
	}
	
	public void setType(TextInputNode.Type type) {
		this.type = type;
	}
	
	public void setTextProperty(ObservableStringProperty textProperty) {
		this.textProperty = textProperty;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		TextInputNode textInputNode = textProperty == null ? new TextInputNode() : new TextInputNode(textProperty);
		entity.addComponent(textInputNode);
		
		if (type != null) {
			textInputNode.type = type;
		}
	}
	
}
