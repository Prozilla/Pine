package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.component.TileRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;

public class TileUpdater extends UpdateSystem{
	
	public TileUpdater() {
		super( new EntityQuery(Transform.class, TileRenderer.class));
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(match -> {
			Transform transform = match.getComponent(Transform.class);
			TileRenderer tileRenderer = match.getComponent(TileRenderer.class);
			
			float x = tileRenderer.coordinate.x * tileRenderer.size;
			float y = tileRenderer.coordinate.y * tileRenderer.size;
			
			transform.setPosition(x, y);
		});
	}
}
