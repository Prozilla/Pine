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
		if (node.size.isZero(node)) {
			return;
		}
		
		int targetSizeX = node.getSizeX();
		int targetSizeY = node.getSizeY();
		
		if (targetSizeX != 0) {
			node.currentSize.x = targetSizeX;
		}
		if (targetSizeY != 0) {
			node.currentSize.y = targetSizeY;
		}
	}
	
	public static void anchorNode(Node node) {
		if (node.currentSize.x == 0 || node.currentSize.y == 0 || node.anchor == null) {
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
		int contextX = 0;
		int contextY = 0;
		
		if (node.absolutePosition) {
			contextX = context.getX();
			contextY = context.getY();
		}
		
		int contextWidth = context.getWidth();
		int contextHeight = context.getHeight();
		
		int remainingWidth = contextWidth - node.currentSize.x;
		int remainingHeight = contextHeight - node.currentSize.y;

		// Calculate offset based on anchor and position
		float offsetX = (1 - 2 * node.anchor.x) * node.getPositionX();
		float offsetY = (1 - 2 * node.anchor.y) * node.getPositionY();
		
		node.currentPosition.x = contextX + Math.round(node.anchor.x * remainingWidth + offsetX);
		node.currentPosition.y = contextY + Math.round(node.anchor.y * remainingHeight + offsetY);
	}
}
