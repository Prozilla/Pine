package dev.prozilla.pine.core.scene;

import dev.prozilla.pine.common.ContextOf;
import dev.prozilla.pine.core.component.camera.CameraData;

@ContextOf(Scene.class)
public interface SceneContext {
	
	World getWorld();
	
	CameraData getCameraData();
	
}
