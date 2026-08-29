package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.Anchor;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TooltipPrefab;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A component for rendering user interfaces.
 */
public class NodeRoot extends Component implements NodeContext {
	
	public Vector2i size;
	
	// Tooltip
	public String currentTooltipText;
	public Entity tooltip;
	public Node tooltipActivator;
	public TooltipCreator tooltipCreator;
	
	// Focus
	public int focusedNodeIndex;
	private Node focusedNode;
	public final List<Node> focusableNodes;
	public boolean isUsingKeyboardNavigation;
	
	@FunctionalInterface
	public interface TooltipCreator {
		
		Entity createTooltip(String text);
		
	}
	
	public NodeRoot() {
		size = new Vector2i();
		tooltipCreator = (text) -> {
			TooltipPrefab tooltipPrefab = new TooltipPrefab();
			tooltipPrefab.setAnchor(Anchor.TOP_LEFT);
			tooltipPrefab.setOffsetX(new Dimension(16));
			tooltipPrefab.setActive(false);
			
			TextPrefab textPrefab = new TextPrefab(text);
			textPrefab.setColor(Color.white());
			textPrefab.setPadding(new DualDimension(8, 4));
			tooltipPrefab.addChild(textPrefab);

			return getScene().addEntity(tooltipPrefab);
		};
		focusedNodeIndex = -1;
		focusedNode = null;
		focusableNodes = new ArrayList<>();
		isUsingKeyboardNavigation = false;
	}
	
	@Override
	public String getName() {
		return "NodeRoot";
	}
	
	@Override
	public float getX() {
		return 0;
	}
	
	@Override
	public float getY() {
		return 0;
	}
	
	@Override
	public float getWidth() {
		return size.x;
	}
	
	@Override
	public float getHeight() {
		return size.y;
	}
	
	public void showTooltip(Node activator, String text) {
		if (Objects.equals(currentTooltipText, text)) {
			return;
		}
		
		// Hide previous tooltip
		if (tooltip != null) {
			hideTooltip();
		}
		
		// Show new tooltip
		tooltip = tooltipCreator.createTooltip(text);
		if (tooltip != null) {
			tooltip.setActive(activator.cursorHit);
			entity.addChild(tooltip);
			
			currentTooltipText = text;
			tooltipActivator = activator;
		}
	}
	
	public void hideTooltip() {
		if (tooltip != null) {
			tooltip = Destructible.destroy(tooltip);
		}
		
		currentTooltipText = null;
		tooltipActivator = null;
	}
	
	public boolean isTooltipShown() {
		return tooltip != null;
	}
	
	public Node getFocusedNode() {
		return focusedNode;
	}
	
	public void focusNextNode(boolean visible) {
		int newFocusedNodeIndex;
		if (focusableNodes.isEmpty()) {
			newFocusedNodeIndex = 0;
		} else {
			newFocusedNodeIndex = (focusedNodeIndex + 1) % focusableNodes.size();
		}
		focusNode(newFocusedNodeIndex, visible);
	}
	
	public void focusPreviousNode(boolean visible) {
		int newFocusedNodeIndex = focusedNodeIndex;
		if (focusableNodes.isEmpty()) {
			newFocusedNodeIndex = 0;
		} else {
			newFocusedNodeIndex--;
			if (newFocusedNodeIndex < 0) {
				newFocusedNodeIndex = focusableNodes.size() - 1;
			}
		}
		focusNode(newFocusedNodeIndex, visible);
	}
	
	public boolean focusNode(Node node, boolean visible) {
		return focusNode(focusableNodes.indexOf(node), visible);
	}
	
	private boolean focusNode(int nodeIndex, boolean visible) {
		if (focusedNodeIndex == nodeIndex) {
			return true;
		}
		
		if (focusedNode != null) {
			focusedNode.removeModifier(Node.FOCUS_MODIFIER);
			focusedNode.removeModifier(Node.FOCUS_VISIBLE_MODIFIER);
			focusedNode.invoke(NodeEvent.Type.BLUR);
		}
		
		focusedNodeIndex = nodeIndex;
		if (focusedNodeIndex < 0 || focusedNodeIndex >= focusableNodes.size()) {
			focusedNode = null;
			return false;
		} else {
			focusedNode = focusableNodes.get(focusedNodeIndex);
			focusedNode.addModifier(Node.FOCUS_MODIFIER);
			if (visible || focusedNode.alwaysVisibleFocus) {
				focusedNode.addModifier(Node.FOCUS_VISIBLE_MODIFIER);
			}
			return true;
		}
	}
	
	public void removeNode(Node node) {
		if (Objects.equals(tooltipActivator, node)) {
			tooltipActivator = null;
		}
	}
	
}
