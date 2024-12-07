package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.core.component.canvas.CanvasRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for user interfaces.
 */
public class CanvasPrefab extends Prefab {
	
	@Override
	protected void apply(Entity entity) {
		entity.addComponent(new CanvasRenderer());
	}
}
