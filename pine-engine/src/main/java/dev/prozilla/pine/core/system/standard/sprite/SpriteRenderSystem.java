package dev.prozilla.pine.core.system.standard.sprite;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

/**
 * Renders sprites to the screen.
 */
public final class SpriteRenderSystem extends RenderSystemBase {
	
	public SpriteRenderSystem() {
		super(SpriteRenderer.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		forEach(chunk -> {
			Transform transform = chunk.getTransform();
			SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
			
			// Calculate screen position
			Vector3f position = transform.position;
			
			// Apply render transformations
			renderer.setMirrorHorizontally(spriteRenderer.mirrorHorizontally);
			renderer.setMirrorVertically(spriteRenderer.mirrorVertically);
			
			// Draw cropped and rotated texture
			if (!spriteRenderer.cropToRegion) {
				renderer.drawRotatedTexture(spriteRenderer.texture, position.x, position.y, position.z, spriteRenderer.color, spriteRenderer.rotation);
			} else {
				renderer.drawRotatedTextureRegion(spriteRenderer.texture, position.x, position.y, position.z,
				 spriteRenderer.regionOffset.x, spriteRenderer.regionOffset.y,
				 spriteRenderer.regionSize.x, spriteRenderer.regionSize.y,
				 spriteRenderer.color, spriteRenderer.rotation);
			}
			
			renderer.resetTransform();
		});
	}
}
