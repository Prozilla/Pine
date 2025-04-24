package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeContext;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

/**
 * Positions nodes based on their offset and anchor point.
 */
public class NodeUpdater extends UpdateSystem {
	
	public NodeUpdater() {
		super( Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Node node = chunk.getComponent(Node.class);
		updateNode(node);
		
		if (!node.isInLayout() || node.absolutePosition) {
			node.readyToRender = true;
		}
	}
	
	public static void updateNode(Node node) {
		resizeNode(node);
		anchorNode(node);
	}
	
	public static void resizeNode(Node node) {
		if (!node.size.isZero(node)) {
			float innerSizeX = node.getInnerSizeX();
			float innerSizeY = node.getInnerSizeY();
			
			if (innerSizeX != 0) {
				node.currentInnerSize.x = innerSizeX;
			}
			if (innerSizeY != 0) {
				node.currentInnerSize.y = innerSizeY;
			}
		}
		
		node.currentOuterSize.x = node.currentInnerSize.x + node.getMarginX() * 2;
		node.currentOuterSize.y = node.currentInnerSize.y + node.getMarginY() * 2;
	}
	
	public static void anchorNode(Node node) {
		if (node.currentInnerSize.x == 0 || node.currentInnerSize.y == 0 || node.anchor == null) {
			return;
		}
		
		NodeContext context;
		try {
			context = node.getContext();
		} catch (RuntimeException e) {
			Logger.system.error("Failed to anchor node: " + node, e);
			return;
		}
		
		// Calculate remaining width and height
		float contextX = 0;
		float contextY = 0;
		
		if (node.absolutePosition) {
			contextX = context.getX();
			contextY = context.getY();
		}
		
		float contextWidth = context.getWidth();
		float contextHeight = context.getHeight();
		
		float remainingWidth = contextWidth - node.currentInnerSize.x;
		float remainingHeight = contextHeight - node.currentInnerSize.y;

		// Calculate offset based on anchor and position
		float offsetX = (1 - 2 * node.anchor.x) * (node.getX());
		float offsetY = (1 - 2 * node.anchor.y) * (node.getY());
		
		node.currentPosition.x = contextX + node.anchor.x * remainingWidth + offsetX;
		node.currentPosition.y = contextY + node.anchor.y * remainingHeight + offsetY;
	}
}
