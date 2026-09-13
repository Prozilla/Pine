package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.camera.OverlayCameraData;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderPass;
import dev.prozilla.pine.core.system.render.SceneRenderSystem;

public class OverlayCameraRenderSystem extends SceneRenderSystem {
	
	public OverlayCameraRenderSystem() {
		setRenderPass(RenderPass.OVERLAY);
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.clearDepthBuffer();
		OverlayCameraData cameraData = scene.getOverlayCameraData();
		cameraData.setSize(application.getWindow().getSize());
		renderer.setProjectionMatrix(cameraData.getProjectionMatrix());
		renderer.setViewMatrix(cameraData.getViewMatrix());
	}
}
