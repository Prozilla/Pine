package dev.prozilla.pine.core.system.standard.sprite;

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
		
		forEach(match -> {
			SpriteRenderer spriteRenderer = match.getComponent(SpriteRenderer.class);
			Entity entity = spriteRenderer.entity;
			
			// Calculate world position
			float worldX = entity.transform.getGlobalX() + spriteRenderer.offsetX;
			float worldY = entity.transform.getGlobalY() + spriteRenderer.offsetY;
			
			// Calculate screen position
			float[] position = camera.applyTransform(worldX, worldY);
			float x = position[0];
			float y = position[1];
			
			// Apply zoom scale
			renderer.setScale(spriteRenderer.scale * camera.getZoom());
			
			// Draw cropped and rotated texture
			if (!spriteRenderer.cropToRegion) {
				renderer.drawRotatedTexture(spriteRenderer.texture, x, y, spriteRenderer.color, spriteRenderer.rotation);
			} else {
				renderer.drawRotatedTextureRegion(spriteRenderer.texture, x, y,
				 spriteRenderer.regionX, spriteRenderer.regionY,
				 spriteRenderer.regionWidth, spriteRenderer.regionHeight,
				 spriteRenderer.color, spriteRenderer.rotation);
			}
		});
	}
}
