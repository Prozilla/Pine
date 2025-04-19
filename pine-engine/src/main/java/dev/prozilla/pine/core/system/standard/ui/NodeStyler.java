package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.NodeStyle;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class NodeStyler extends UpdateSystem {
	
	public NodeStyler() {
		super( Node.class, NodeStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Node node = chunk.getComponent(Node.class);
		NodeStyle nodeStyle = chunk.getComponent(NodeStyle.class);
		
		if (nodeStyle.getColorProperty() != null) {
			if (node.color == null) {
				node.color = nodeStyle.getColorProperty().getValue();
			} else {
				nodeStyle.getColorProperty().transmit(node.color);
			}
		}
		if (nodeStyle.getBackgroundColorProperty() != null) {
			if (node.backgroundColor == null) {
				node.backgroundColor = nodeStyle.getBackgroundColorProperty().getValue();
			} else {
				nodeStyle.getBackgroundColorProperty().transmit(node.backgroundColor);
			}
		}
		if (nodeStyle.getSizeProperty() != null) {
			node.size = nodeStyle.getSizeProperty().getValue();
		}
		if (nodeStyle.getPaddingProperty() != null) {
			node.padding = nodeStyle.getPaddingProperty().getValue();
		}
		if (nodeStyle.getMarginProperty() != null) {
			node.margin = nodeStyle.getMarginProperty().getValue();
		}
		if (nodeStyle.getMarginProperty() != null && !node.isInLayout()) {
			node.margin = nodeStyle.getMarginProperty().getValue();
		}
		if (nodeStyle.getAnchorProperty() != null && !node.isInLayout()) {
			node.anchor = nodeStyle.getAnchorProperty().getValue();
		}
	}
}
