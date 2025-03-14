package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.random.property.FixedProperty;
import dev.prozilla.pine.common.random.property.VariableProperty;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.Texture;
import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;

public class ParticlePrefab extends SpritePrefab {
	
	protected VariableProperty<Float> lifetime;
	protected VariableProperty<Vector2f> velocity;
	protected VariableProperty<Float> scale;
	protected boolean applyTimeScale;
	
	protected int frameCount;
	protected boolean animateSprite;
	protected VariableProperty<Integer> initialFrame;
	
	public static final VariableProperty<Float> DEFAULT_LIFETIME = new FixedProperty<>(5f);
	
	public ParticlePrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public ParticlePrefab(Texture texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticlePrefab(String texturePath, VariableProperty<Float> lifetime) {
		this(ResourcePool.loadTexture(texturePath), lifetime);
	}
	
	public ParticlePrefab(Texture texture, VariableProperty<Float> lifetime) {
		super(texture);
		this.lifetime = lifetime;
		frameCount = ParticleRenderer.DEFAULT_FRAME_COUNT;
		animateSprite = ParticleRenderer.ANIMATE_SPRITE_DEFAULT;
		applyTimeScale = ParticleRenderer.APPLY_TIME_SCALE_DEFAULT;
	}
	
	@Override
	public void setScale(float scale) {
		setScale(new FixedProperty<>(scale));
	}
	
	public void setScale(VariableProperty<Float> scale) {
		this.scale = scale;
	}
	
	public void setLifetime(VariableProperty<Float> lifetime) {
		this.lifetime = lifetime;
	}
	
	public void setVelocity(VariableProperty<Vector2f> velocity) {
		this.velocity = velocity;
	}
	
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
	
	public void setAnimateSprite(boolean animateSprite) {
		this.animateSprite = animateSprite;
	}
	
	public void setInitialFrame(VariableProperty<Integer> initialFrame) {
		this.initialFrame = initialFrame;
	}
	
	public void setApplyTimeScale(boolean applyTimeScale) {
		this.applyTimeScale = applyTimeScale;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		if (scale != null) {
			SpriteRenderer spriteRenderer = entity.getComponent(SpriteRenderer.class);
			spriteRenderer.scale = scale.getValue();
		}
		
		ParticleRenderer particleRenderer = new ParticleRenderer(lifetime.getValue());
		particleRenderer.frameCount = frameCount;
		particleRenderer.animateSprite = animateSprite;
		particleRenderer.applyTimeScale = applyTimeScale;
		
		if (velocity != null) {
			particleRenderer.velocity = velocity.getValue();
		}
		if (initialFrame != null) {
			particleRenderer.initialFrame = initialFrame.getValue();
		}
		
		entity.addComponent(particleRenderer);
	}
}
