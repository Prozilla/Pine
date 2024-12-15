package dev.prozilla.pine.core.entity.prefab.canvas;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.canvas.RectRenderer;
import dev.prozilla.pine.core.entity.Entity;

public class RectPrefab extends CanvasElementPrefab {
	
	protected Color color;
	
	public RectPrefab() {
		this(null);
	}
	
	public RectPrefab(Color color) {
		super();
		setName("Rect");
		
		this.color = color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new RectRenderer(color));
	}
}
