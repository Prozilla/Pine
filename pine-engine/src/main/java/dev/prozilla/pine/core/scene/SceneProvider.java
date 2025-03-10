package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.core.component.camera.CameraData;

public interface SceneProvider extends SceneContext {
	
	Scene getScene();
	
	@Override
	default World getWorld() {
		return getScene().getWorld();
	}
	
	@Override
	default CameraData getCameraData() {
		return getScene().getCameraData();
	}
	
}
