package dev.prozilla.pine.core.system.render;

import dev.prozilla.pine.core.component.ComponentCollector;
import dev.prozilla.pine.core.component.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.camera.Camera;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.RenderSystem;

public class SpriteRenderSystem extends RenderSystem {
	
	public SpriteRenderSystem() {
		super(new ComponentCollector(SpriteRenderer.class));
	}
	
	@Override
	public void render(Renderer renderer) {
		Camera camera = world.scene.getCamera();
		
		forEach(componentGroup -> {
			SpriteRenderer spriteRenderer = componentGroup.getComponent(SpriteRenderer.class);
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