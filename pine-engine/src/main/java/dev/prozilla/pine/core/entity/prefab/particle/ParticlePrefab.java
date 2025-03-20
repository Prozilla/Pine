package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.VariableColorProperty;
import dev.prozilla.pine.common.property.VariableProperty;
import dev.prozilla.pine.common.property.animated.AnimatedProperty;
import dev.prozilla.pine.common.property.animated.VariableAnimatedColorProperty;
import dev.prozilla.pine.common.property.animated.VariableAnimatedFloatProperty;
import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.common.system.resource.ResourcePool;
import dev.prozilla.pine.common.system.resource.TextureBase;
import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;

public class ParticlePrefab extends SpritePrefab {
	
	protected VariableProperty<Color> color;
	protected VariableProperty<Float> lifetime;
	protected VariableProperty<Vector2f> velocity;
	protected VariableProperty<Float> scale;
	protected boolean applyTimeScale;
	
	protected VariableAnimatedFloatProperty scaleAnimation;
	protected VariableAnimatedColorProperty colorAnimation;
	
	protected int frameCount;
	protected boolean animateSprite;
	protected VariableProperty<Integer> initialFrame;
	
	public static final VariableProperty<Float> DEFAULT_LIFETIME = new FixedProperty<>(5f);
	
	public ParticlePrefab(String texturePath) {
		this(ResourcePool.loadTexture(texturePath));
	}
	
	public ParticlePrefab(TextureBase texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticlePrefab(String texturePath, VariableProperty<Float> lifetime) {
		this(ResourcePool.loadTexture(texturePath), lifetime);
	}
	
	public ParticlePrefab(TextureBase texture, VariableProperty<Float> lifetime) {
		super(texture);
		this.lifetime = lifetime;
		frameCount = ParticleRenderer.DEFAULT_FRAME_COUNT;
		animateSprite = ParticleRenderer.ANIMATE_SPRITE_DEFAULT;
		applyTimeScale = ParticleRenderer.APPLY_TIME_SCALE_DEFAULT;
	}
	
	@Override
	public void setColor(Color color) {
		setColor(new FixedProperty<>(color));
	}
	
	public void setColor(VariableProperty<Color> color) {
		this.color = color;
		colorAnimation = null;
	}
	
	/**
	 * Adds an animation for the alpha value of the particle's color, going from <code>1f</code> to <code>0f</code>.
	 */
	public void setColorAlphaAnimation(Color base, EasingFunction easingFunction) {
		setColorAlphaAnimation(base, new FixedProperty<>(1f), new FixedProperty<>(0f), easingFunction);
	}
	
	/**
	 * Adds a linear animation for the alpha value of the particle's color.
	 */
	public void setColorAlphaAnimation(Color base, VariableProperty<Float> alphaStart, VariableProperty<Float> alphaEnd) {
		setColorAlphaAnimation(base, alphaStart, alphaEnd, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the alpha value of the particle's color.
	 */
	public void setColorAlphaAnimation(Color base, VariableProperty<Float> alphaStart, VariableProperty<Float> alphaEnd, EasingFunction easingFunction) {
		VariableColorProperty start = new VariableColorProperty(base);
		VariableColorProperty end = new VariableColorProperty(base);
		start.setAlpha(alphaStart);
		end.setAlpha(alphaEnd);
		
		setColorAnimation(start, end, easingFunction);
	}
	
	/**
	 * Adds a linear animation for the color of the particle.
	 */
	public void setColorAnimation(VariableProperty<Color> start, VariableProperty<Color> end) {
		setColorAnimation(start, end, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the color of the particle.
	 */
	public void setColorAnimation(VariableProperty<Color> start, VariableProperty<Color> end, EasingFunction easingFunction) {
		colorAnimation = new VariableAnimatedColorProperty(start, end, new FixedProperty<>(1f), new FixedProperty<>(easingFunction));
		color = null;
	}
	
	@Override
	public void setScale(float scale) {
		setScale(new FixedProperty<>(scale));
	}
	
	public void setScale(VariableProperty<Float> scale) {
		this.scale = scale;
		scaleAnimation = null;
	}
	
	/**
	 * Adds a linear animation for the scale of the particle.
	 */
	public void setScaleAnimation(VariableProperty<Float> start, VariableProperty<Float> end) {
		setScaleAnimation(start, end, AnimatedProperty.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the scale of the particle.
	 */
	public void setScaleAnimation(VariableProperty<Float> start, VariableProperty<Float> end, EasingFunction easingFunction) {
		scaleAnimation = new VariableAnimatedFloatProperty(start, end, new FixedProperty<>(1f), new FixedProperty<>(easingFunction));
		scale = null;
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
	
	public void setFrame(VariableProperty<Integer> frame) {
		setAnimateSprite(false);
		setInitialFrame(frame);
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
		
		SpriteRenderer spriteRenderer = entity.getComponent(SpriteRenderer.class);
		
		ParticleRenderer particleRenderer = new ParticleRenderer(lifetime.getValue());
		particleRenderer.frameCount = frameCount;
		particleRenderer.animateSprite = animateSprite;
		particleRenderer.applyTimeScale = applyTimeScale;
		
		// Apply scale
		if (scale != null) {
			spriteRenderer.scale = scale.getValue();
		} else if (scaleAnimation != null) {
			particleRenderer.scaleAnimation = scaleAnimation.getValue();
		}
		
		// Apply color
		if (color != null) {
			spriteRenderer.color.copyFrom(color.getValue());
		} else if (colorAnimation != null) {
			particleRenderer.colorAnimation = colorAnimation.getValue();
		}
		
		// Apply velocity
		if (velocity != null) {
			particleRenderer.velocity = velocity.getValue();
		}
		
		// Apply initial frame
		if (initialFrame != null) {
			particleRenderer.initialFrame = initialFrame.getValue();
		}
		
		entity.addComponent(particleRenderer);
	}
}
