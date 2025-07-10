package dev.prozilla.pine.core.entity.prefab.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

/**
 * Prefab for 2D rectangles.
 */
@Components({ RectRenderer.class, Transform.class })
public class RectPrefab extends Prefab {
	
	protected Vector2f size;
	protected Color color;
	
	public RectPrefab(Vector2f size) {
		this(size, null);
	}
	
	public RectPrefab(Vector2f size, Color color) {
		this.size = Checks.isNotNull(size, "size");
		this.color = color;
		
		setName("Rect");
	}
	
	public void setSize(Vector2f size) {
		this.size = Checks.isNotNull(size, "size");
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		RectRenderer rectRenderer = color != null ? new RectRenderer(size.clone(), color.clone()) : new RectRenderer(size.clone());
		entity.addComponent(rectRenderer);
	}
}
