package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.entity.prefab.ui.TooltipPrefab;

/**
 * A component for rendering user interfaces.
 */
public class NodeRoot extends Component implements NodeContext {
	
	public Vector2i size;
	
	public String currentTooltipText;
	public Entity tooltip;
	public Node tooltipActivator;
	public TooltipCreator tooltipCreator;
	
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
			Entity tooltip = tooltipPrefab.instantiate(getWorld());
			
			TextPrefab textPrefab = new TextPrefab(text);
			textPrefab.setColor(Color.white());
			tooltip.addChild(textPrefab);
			
			return tooltip;
		};
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
		if (currentTooltipText != null && currentTooltipText.equals(text)) {
			return;
		}
		
		if (tooltip != null) {
			tooltip.destroy();
		}
		
		tooltip = tooltipCreator.createTooltip(text);
		tooltip.setActive(activator.cursorHit);
		entity.addChild(tooltip);
		
		currentTooltipText = text;
		tooltipActivator = activator;
	}
	
	public void hideTooltip() {
		if (tooltip != null) {
			tooltip.destroy();
		}
		
		currentTooltipText = null;
		tooltipActivator = null;
	}
}
