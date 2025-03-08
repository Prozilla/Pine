package dev.prozilla.pine.core.system.standard.canvas.tooltip;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.core.component.canvas.RectTransform;
import dev.prozilla.pine.core.component.canvas.TooltipRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class TooltipInitializer extends InitSystem {
	
	public TooltipInitializer() {
		super(TooltipRenderer.class, RectTransform.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		TooltipRenderer tooltipRenderer = chunk.getComponent(TooltipRenderer.class);
		RectTransform rect = chunk.getComponent(RectTransform.class);
		
		rect.position = new DualDimension(
			Dimension.add(tooltipRenderer.cursorX, tooltipRenderer.baseX, tooltipRenderer.offset.x),
			Dimension.add(tooltipRenderer.cursorY, tooltipRenderer.baseY, tooltipRenderer.offset.y)
		);
	}
}
