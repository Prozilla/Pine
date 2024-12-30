package dev.prozilla.pine.core.component.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.FrameBufferObject;

public class FrameRenderer extends Component {
	
	public FrameBufferObject fbo;
	public Color backgroundColor;
	
	public FrameRenderer() {
		this(null);
	}
	
	public FrameRenderer(FrameBufferObject fbo) {
		this.fbo = fbo;
	}
	
	public int getWidth() {
		if (fbo == null)
			return 0;
		
		return fbo.getWidth();
	}
	
	public int getHeight() {
		if (fbo == null)
			return 0;
		
		return fbo.getHeight();
	}
}
