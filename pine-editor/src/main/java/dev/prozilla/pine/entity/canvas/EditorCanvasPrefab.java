package dev.prozilla.pine.entity.canvas;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.canvas.CanvasPrefab;
import dev.prozilla.pine.entity.canvas.header.HeaderPrefab;

public class EditorCanvasPrefab extends CanvasPrefab {
	
	protected Application preview;
	
	public EditorCanvasPrefab(Application preview) {
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
