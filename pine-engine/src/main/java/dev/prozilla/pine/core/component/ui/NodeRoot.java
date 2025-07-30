package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
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
	
	public String currentTooltipText;
	public Entity tooltip;
	public Node tooltipActivator;
	public TooltipCreator tooltipCreator;
	
	public int focusedNodeIndex;
	private Node focusedNode;
	public final List<Node> focusableNodes;
	
	@FunctionalInterface
	public interface TooltipCreator {
		
		Entity createTooltip(String text);
		
	}
	
	public NodeRoot() {
		size = new Vector2i();
		tooltipCreator = (text) -> {
			TooltipPrefab tooltipPrefab = new TooltipPrefab();
			tooltipPrefab.setAnchor(GridAlignment.TOP_LEFT);
			tooltipPrefab.setOffsetX(new Dimension(16));
			tooltipPrefab.setActive(false);
			
			TextPrefab textPrefab = new TextPrefab(text);
			textPrefab.setColor(Color.white());
			textPrefab.setPadding(new DualDimension(8, 4));
			tooltipPrefab.addChild(textPrefab);

			return getWorld().addEntity(tooltipPrefab);
		};
		focusedNodeIndex = -1;
		focusedNode = null;
		focusableNodes = new ArrayList<>();
	}
	
	@Override
	public String getName() {
		return "CanvasRenderer";
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
			tooltip.destroy();
		}
		
		// Show new tooltip
		tooltip = tooltipCreator.createTooltip(text);
		if (tooltip != null) {
			tooltip.setActive(activator.cursorHit);
			entity.addChild(tooltip);
			
			currentTooltipText = text;
			tooltipActivator = activator;
		} else {
			currentTooltipText = null;
			tooltipActivator = null;
		}
	}
	
	public void hideTooltip() {
		if (tooltip != null) {
			tooltip.destroy();
		}
		
		currentTooltipText = null;
		tooltipActivator = null;
	}
	
	public Node getFocusedNode() {
		return focusedNode;
	}
	
	public void focusNextNode() {
		int newFocusedNodeIndex;
		if (focusableNodes.isEmpty()) {
			newFocusedNodeIndex = 0;
		} else {
			newFocusedNodeIndex = (focusedNodeIndex + 1) % focusableNodes.size();
		}
		focusNode(newFocusedNodeIndex);
	}
	
	public void focusPreviousNode() {
		int newFocusedNodeIndex = focusedNodeIndex;
		if (focusableNodes.isEmpty()) {
			newFocusedNodeIndex = 0;
		} else {
			newFocusedNodeIndex--;
			if (newFocusedNodeIndex < 0) {
				newFocusedNodeIndex = focusableNodes.size() - 1;
			}
		}
		focusNode(newFocusedNodeIndex);
	}
	
	public void focusNode(Node node) {
		focusNode(focusableNodes.indexOf(node));
	}
	
	private void focusNode(int nodeIndex) {
		if (focusedNodeIndex == nodeIndex) {
			return;
		}
		
		if (focusedNode != null) {
			focusedNode.removeModifier(Node.FOCUS_MODIFIER);
		}
		
		focusedNodeIndex = nodeIndex;
		if (focusedNodeIndex < 0 || focusedNodeIndex >= focusableNodes.size()) {
			focusedNode = null;
		} else {
			focusedNode = focusableNodes.get(focusedNodeIndex);
			focusedNode.addModifier(Node.FOCUS_MODIFIER);
		}
	}
	
}
