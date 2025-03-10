package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.core.component.camera.CameraData;

public interface SceneContext {
	
	World getWorld();
	
	CameraData getCameraData();
	
}
