package dev.prozilla.pine.core.system.update;

import dev.prozilla.pine.core.entity.EntityQuery;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.system.UpdateSystem;

public class CameraUpdater extends UpdateSystem {
	
	public CameraUpdater() {
		super(new EntityQuery(CameraData.class));
	}
	
	@Override
	public void update(float deltaTime) {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(match -> {
			CameraData cameraData = match.getComponent(CameraData.class);
			
			cameraData.setSize(width, height);
		});
	}
}
