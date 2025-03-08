package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.core.component.Component;

public class TooltipRenderer extends Component {
	
	public Dimension cursorX;
	public Dimension cursorY;
	public Dimension baseX;
	public Dimension baseY;
	
	public DualDimension offset;
	
	public TooltipRenderer() {
		this(new DualDimension());
	}
	
	public TooltipRenderer(DualDimension offset) {
		this.offset = offset;
		
		cursorX = new Dimension();
		cursorY = new Dimension();
		baseX = new Dimension();
		baseY = new Dimension();
	}
}
