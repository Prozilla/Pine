package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.TileRenderer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.system.UpdateSystem;

public class TileUpdater extends UpdateSystem{
	
	public TileUpdater() {
		super( new ComponentCollector(Transform.class, TileRenderer.class));
	}
	
	@Override
	public void update(float deltaTime) {
		forEach(componentGroup -> {
			Transform transform = componentGroup.getComponent(Transform.class);
			TileRenderer tileRenderer = componentGroup.getComponent(TileRenderer.class);
			
			float x = tileRenderer.coordinate.x * tileRenderer.size;
			float y = tileRenderer.coordinate.y * tileRenderer.size;
			
			transform.setPosition(x, y);
		});
	}
}
