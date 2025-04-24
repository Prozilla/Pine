package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.TooltipNode;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.system.input.InputSystemBase;

import java.util.Comparator;

/**
 * Handles clicks on nodes.
 */
public class NodeInputHandler extends InputSystemBase {
	
	public NodeInputHandler() {
		super(Node.class);
		setExcludedComponentTypes(TooltipNode.class);
	}
	
	@Override
	public void input(float deltaTime) {
		Input input = getInput();
		Vector2i cursor = input.getCursor();
		
		forEachReverse(chunk -> {
			Entity entity = chunk.getEntity();
			Node node = chunk.getComponent(Node.class);
			
			boolean cursorHit = false;
			
			if (!node.passThrough && !node.isInTooltip() && !input.isCursorBlocked()) {
				float canvasHeight = node.getRoot().getHeight();
				if (cursor != null && Node.isInsideRect(new Vector2f(cursor.x, canvasHeight - cursor.y), node.currentPosition, node.currentInnerSize)) {
					cursorHit = true;
					input.blockCursor(entity);
				}
			}
			
			boolean isChildHovered = false;
			Entity cursorBlocker = input.getCursorBlocker();
			if (cursorBlocker != null && cursorBlocker.isDescendantOf(entity)) {
				isChildHovered = true;
			}
			
			// Apply hover modifier when node or child node is hovered
			node.toggleModifier("hover", cursorHit || isChildHovered);
			
			node.cursorHit = cursorHit;
			
			if (node.tooltipText != null && node.cursorHit) {
				node.getRoot().showTooltip(node, node.tooltipText);
			}
		});
	}
	
	@Override
	public void sort() {
		sort(Comparator.comparingInt(a -> a.getTransform().getDepthIndex()));
	}
}
