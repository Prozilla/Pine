package dev.prozilla.pine.core.system.standard.particle;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class ParticleUpdater extends UpdateSystem {
	
	public ParticleUpdater() {
		super(ParticleRenderer.class, SpriteRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getTransform();
		ParticleRenderer particleRenderer = chunk.getComponent(ParticleRenderer.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		
		if (particleRenderer.applyTimeScale) {
			deltaTime = application.getTimer().getScaledDeltaTime();
		}
		
		// Update lifetime
		particleRenderer.lifetime -= deltaTime;
		if (particleRenderer.lifetime <= 0) {
			particleRenderer.kill();
			return;
		}
		
		// Update sprite region based on current frame
		if (particleRenderer.animateSprite) {
			int currentFrame = (int)Math.floor((particleRenderer.lifetime / particleRenderer.initialLifetime) * particleRenderer.frameCount);
			float textureHeight = (float)spriteRenderer.texture.getHeight() / particleRenderer.frameCount;
			spriteRenderer.setRegion(0, currentFrame * textureHeight, spriteRenderer.texture.getWidth(), textureHeight);
		}
		
		// Update position based on velocity
		if (particleRenderer.velocity != null) {
			transform.translate(particleRenderer.velocity.x * deltaTime, particleRenderer.velocity.y * deltaTime);
		}
		
		// Update scale based on animation
		if (particleRenderer.scaleAnimation != null) {
			spriteRenderer.scale.set(particleRenderer.scaleAnimation.getUpdatedValue(deltaTime));
		}
		
		// Update color based on animation
		if (particleRenderer.colorAnimation != null) {
			particleRenderer.colorAnimation.applyUpdatedValue(deltaTime, spriteRenderer.color);
		}
	}
}
