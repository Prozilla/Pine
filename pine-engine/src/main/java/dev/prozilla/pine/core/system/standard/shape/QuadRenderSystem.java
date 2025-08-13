package dev.prozilla.pine.core.system.standard.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.shape.QuadRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

public final class QuadRenderSystem extends RenderSystemBase {
	
	public QuadRenderSystem() {
		super(QuadRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		CameraData camera = scene.getCameraData();
		
		forEach(chunk -> {
			Transform transform = chunk.getTransform();
			QuadRenderer quadRenderer = chunk.getComponent(QuadRenderer.class);
			
			if (quadRenderer.size.x == 0 && quadRenderer.size.y == 0) {
				return;
			}
			
			// Calculate world position
			float worldX = transform.getGlobalX();
			float worldY = transform.getGlobalY();
			
			// Calculate screen position
			Vector2f position = camera.applyTransform(worldX, worldY);
			
			// Apply render transformations
			renderer.setScale(camera.getZoom());
			
			renderer.drawRect(position.x, position.y, transform.getDepth(), quadRenderer.size.x, quadRenderer.size.y, quadRenderer.color);
			
			renderer.resetTransform();
		});
	}
	
}
