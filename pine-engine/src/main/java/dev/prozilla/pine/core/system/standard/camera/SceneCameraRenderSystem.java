package dev.prozilla.pine.core.system.standard.camera;

import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.material.SkyboxMaterial;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.system.render.SceneRenderSystem;

public final class SceneCameraRenderSystem extends SceneRenderSystem {
	
	@Override
	public void render(Renderer renderer) {
		CameraData cameraData = scene.getCameraData();
		renderer.setProjectionMatrix(cameraData.getProjectionMatrix());
		renderer.setViewMatrix(cameraData.getViewMatrix());
		renderSkybox(renderer, cameraData.skybox);
	}
	
	private void renderSkybox(Renderer renderer, SkyboxMaterial material) {
		if (!material.hasCubemap()) {
			return;
		}
		
		ShaderProgram.getSkybox().drawSkybox(renderer, material);
	}
	
}
