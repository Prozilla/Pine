package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.style.NodeStyle;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public final class NodeStyler extends UpdateSystem {
	
	public NodeStyler() {
		super( Node.class, NodeStyle.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Node node = chunk.getComponent(Node.class);
		NodeStyle nodeStyle = chunk.getComponent(NodeStyle.class);
		
		if (nodeStyle.getColorProperty() != null) {
			node.color = nodeStyle.getColorProperty().getValue();
		}
		if (nodeStyle.getBackgroundColorProperty() != null) {
			node.backgroundColor = nodeStyle.getBackgroundColorProperty().getValue();
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
		if (nodeStyle.getAnchorProperty() != null && (!node.isInLayout() || node.absolutePosition)) {
			node.anchor = nodeStyle.getAnchorProperty().getValue();
		}
		if (nodeStyle.getCursorProperty() != null) {
			node.cursor = nodeStyle.getCursorProperty().getValue();
		}
		if (nodeStyle.getBorderWidthProperty() != null) {
			node.borderWidth = nodeStyle.getBorderWidthProperty().getValue();
		}
		if (nodeStyle.getBorderStyleProperty() != null) {
			node.borderStyle = nodeStyle.getBorderStyleProperty().getValue();
		}
		if (nodeStyle.getBorderColorProperty() != null) {
			node.borderColor = nodeStyle.getBorderColorProperty().getValue();
		}
		if (nodeStyle.getOutlineWidthProperty() != null) {
			node.outlineWidth = nodeStyle.getOutlineWidthProperty().getValue();
		}
		if (nodeStyle.getOutlineStyleProperty() != null) {
			node.outlineStyle = nodeStyle.getOutlineStyleProperty().getValue();
		}
		if (nodeStyle.getOutlineColorProperty() != null) {
			node.outlineColor = nodeStyle.getOutlineColorProperty().getValue();
		}
		if (nodeStyle.getOutlineOffsetProperty() != null) {
			node.outlineOffset = nodeStyle.getOutlineOffsetProperty().getValue();
		}
	}
}
