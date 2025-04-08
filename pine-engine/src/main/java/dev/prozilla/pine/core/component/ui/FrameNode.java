package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.rendering.FrameBufferObject;

public class FrameNode extends Component {
	
	public FrameBufferObject fbo;
	public Color backgroundColor;
	
	public FrameNode() {
		this(null);
	}
	
	public FrameNode(FrameBufferObject fbo) {
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
