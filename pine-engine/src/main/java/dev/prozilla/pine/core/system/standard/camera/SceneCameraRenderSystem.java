package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.SceneRenderSystem;

public class SceneCameraRenderSystem extends SceneRenderSystem {
	
	@Override
	public void render(Renderer renderer) {
		CameraData cameraData = scene.getCameraData();
		renderer.setProjectionMatrix(cameraData.getProjectionMatrix());
		renderer.setViewMatrix(cameraData.getViewMatrix());
	}
}
