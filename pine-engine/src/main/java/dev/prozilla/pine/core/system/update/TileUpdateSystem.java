package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.TileRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;

public class TileUpdateSystem extends UpdateSystem<TileRenderer> {
	
	public TileUpdateSystem() {
		super(TileRenderer.class);
	}
	
	@Override
	public void update(float deltaTime) {
		for (TileRenderer tileRenderer : getComponents()) {
			Transform transform = tileRenderer.getTransform();
			
			float x = tileRenderer.coordinate.x * tileRenderer.size;
			float y = tileRenderer.coordinate.y * tileRenderer.size;
			
			transform.setPosition(x, y);
		}
	}
}
