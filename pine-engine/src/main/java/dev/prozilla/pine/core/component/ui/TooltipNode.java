package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.dimension.Dimension;
import dev.prozilla.pine.common.math.dimension.DualDimension;
import dev.prozilla.pine.core.component.Component;

/**
 * A component for rendering a tooltip that follows the cursor.
 */
public class TooltipNode extends Component {
	
	public Dimension cursorX;
	public Dimension cursorY;
	public Dimension baseX;
	public Dimension baseY;
	
	public DualDimension offset;
	
	public TooltipNode() {
		this(new DualDimension());
	}
	
	public TooltipNode(DualDimension offset) {
		this.offset = offset;
		
		cursorX = new Dimension();
		cursorY = new Dimension();
		baseX = new Dimension();
		baseY = new Dimension();
	}
}
