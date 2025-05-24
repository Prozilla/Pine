package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.FrameNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.rendering.FrameBufferObject;

@Components({ FrameNode.class, Node.class, Transform.class })
public class FramePrefab extends NodePrefab {
	
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
		
		FrameNode frameNode = entity.addComponent(new FrameNode(fbo));
		frameNode.backgroundColor = backgroundColor;
	}
}
