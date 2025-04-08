package dev.prozilla.pine.entity.canvas;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.ui.NodeRootPrefab;
import dev.prozilla.pine.entity.canvas.header.HeaderPrefab;

public class EditorNodeRootPrefab extends NodeRootPrefab {
	
	protected Application preview;
	
	public EditorNodeRootPrefab(Application preview) {
		super();
		this.preview = preview;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addChild(new PreviewFramePrefab(preview));
		
		entity.addChild(new HeaderPrefab());
	}
}
