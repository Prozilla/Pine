package dev.prozilla.pine.core.system.standard.particle;

import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class ParticleInitializer extends InitSystem {
	
	public ParticleInitializer() {
		super(ParticleRenderer.class, SpriteRenderer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		ParticleRenderer particleRenderer = chunk.getComponent(ParticleRenderer.class);
		SpriteRenderer spriteRenderer = chunk.getComponent(SpriteRenderer.class);
		
		float textureWidth = spriteRenderer.texture.getWidth();
		float textureHeight = spriteRenderer.texture.getHeight() / (float)particleRenderer.frameCount;
		spriteRenderer.setRegion(0, textureHeight * (particleRenderer.frameCount - 1), textureWidth, textureHeight);
		
		spriteRenderer.offset.x = -textureWidth / 2f;
		spriteRenderer.offset.y = -textureHeight / 2f;
	}
}
