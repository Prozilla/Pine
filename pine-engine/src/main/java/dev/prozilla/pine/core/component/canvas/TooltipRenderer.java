package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.core.component.Component;

public class TooltipRenderer extends Component {
	
	public Dimension offsetX;
	public Dimension offsetY;
	public Dimension cursorX;
	public Dimension cursorY;
	
	public TooltipRenderer() {
		offsetX = new Dimension();
		offsetY = new Dimension();
		cursorX = new Dimension();
		cursorY = new Dimension();
	}
}
