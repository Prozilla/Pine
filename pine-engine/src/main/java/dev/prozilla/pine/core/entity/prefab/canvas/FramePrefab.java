package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.FrameRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.FrameBufferObject;

public class FramePrefab extends CanvasElementPrefab {
	
	protected FrameBufferObject fbo;
	protected Color backgroundColor;
	
	public FramePrefab() {
		this(null);
	}
	
	public FramePrefab(FrameBufferObject fbo) {
		super();
		setName("Frame");
		addClass("frame");
		
		this.fbo = fbo;
	}
	
	public void setFrameBufferObject(FrameBufferObject fbo) {
		this.fbo = fbo;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		FrameRenderer frameRenderer = entity.addComponent(new FrameRenderer(fbo));
		frameRenderer.backgroundColor = backgroundColor;
	}
}
