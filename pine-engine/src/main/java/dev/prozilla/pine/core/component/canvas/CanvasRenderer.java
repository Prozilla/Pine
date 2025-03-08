package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.vector.GridAlignment;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.TextPrefab;
import dev.prozilla.pine.core.entity.prefab.canvas.TooltipPrefab;

/**
 * A component for rendering user interfaces.
 */
public class CanvasRenderer extends Component implements CanvasContext {
	
	public Vector2i size;
	
	public String currentTooltipText;
	public Entity tooltip;
	public RectTransform tooltipActivator;
	public TooltipCreator tooltipCreator;
	
	@FunctionalInterface
	public interface TooltipCreator {
		
		Entity createTooltip(String text);
		
	}
	
	public CanvasRenderer() {
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
	public int getX() {
		return 0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return size.x;
	}
	
	@Override
	public int getHeight() {
		return size.y;
	}
	
	public void showTooltip(RectTransform activator, String text) {
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
