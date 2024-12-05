package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.UpdateSystem;

public class CameraUpdater extends UpdateSystem {
	
	public CameraUpdater() {
		super(new ComponentCollector(CameraData.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int width = world.application.getWindow().getWidth();
		int height = world.application.getWindow().getHeight();
		
		forEach(componentGroup -> {
			CameraData cameraData = componentGroup.getComponent(CameraData.class);
			
			cameraData.setSize(width, height);
		});
	}
}
