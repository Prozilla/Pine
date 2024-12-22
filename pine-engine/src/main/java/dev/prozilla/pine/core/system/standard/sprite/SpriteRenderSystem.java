package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

/**
 * Renders sprites to the screen.
 */
public class SpriteRenderSystem extends RenderSystemBase {
	
	public SpriteRenderSystem() {
		super(SpriteRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		CameraData camera = scene.getCameraData();
		
		forEach(chunk -> {
			Transform transform = chunk.getTransform();
			SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
			Entity entity = spriteRenderer.entity;
			
			// Calculate world position
			float worldX = entity.transform.getGlobalX() + spriteRenderer.offset.x;
			float worldY = entity.transform.getGlobalY() + spriteRenderer.offset.y;
			
			// Calculate screen position
			Vector2f position = camera.applyTransform(worldX, worldY);
			
			// Apply zoom scale
			renderer.setScale(spriteRenderer.scale * camera.getZoom());
			
			// Draw cropped and rotated texture
			if (!spriteRenderer.cropToRegion) {
				renderer.drawRotatedTexture(spriteRenderer.texture, position.x, position.y, transform.getDepth(), spriteRenderer.color, spriteRenderer.rotation);
			} else {
				renderer.drawRotatedTextureRegion(spriteRenderer.texture, position.x, position.y, transform.getDepth(),
				 spriteRenderer.regionOffset.x, spriteRenderer.regionOffset.y,
				 spriteRenderer.regionSize.x, spriteRenderer.regionSize.y,
				 spriteRenderer.color, spriteRenderer.rotation);
			}
		});
	}
}
