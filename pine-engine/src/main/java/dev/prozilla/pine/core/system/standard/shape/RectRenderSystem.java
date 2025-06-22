package dev.prozilla.pine.core.system.standard.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

public class RectRenderSystem extends RenderSystemBase {
	
	public RectRenderSystem() {
		super(RectRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		CameraData camera = scene.getCameraData();
		
		forEach(chunk -> {
			Transform transform = chunk.getTransform();
			RectRenderer rectRenderer = chunk.getComponent(RectRenderer.class);
			
			if (rectRenderer.size.x == 0 && rectRenderer.size.y == 0) {
				return;
			}
			
			// Calculate world position
			float worldX = transform.getGlobalX();
			float worldY = transform.getGlobalY();
			
			// Calculate screen position
			Vector2f position = camera.applyTransform(worldX, worldY);
			
			// Apply render transformations
			renderer.setScale(camera.getZoom());
			
			renderer.drawRect(position.x, position.y, transform.getDepth(), rectRenderer.size.x, rectRenderer.size.y, rectRenderer.color);
			
			renderer.resetTransform();
		});
	}
	
}
