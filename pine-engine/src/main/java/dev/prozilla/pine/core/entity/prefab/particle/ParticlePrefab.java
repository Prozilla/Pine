package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.ColorProperty;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.IntProperty;
import dev.prozilla.pine.common.property.VariableColorProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.variable.VariableAnimatedColorProperty;
import dev.prozilla.pine.common.property.animated.variable.VariableAnimatedFloatProperty;
import dev.prozilla.pine.common.property.fixed.FixedColorProperty;
import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.property.vector.Vector2fProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;

public class ParticlePrefab extends SpritePrefab {
	
	protected ColorProperty color;
	protected FloatProperty lifetime;
	protected Vector2fProperty velocity;
	protected FloatProperty scale;
	protected boolean applyTimeScale;
	
	protected VariableAnimatedFloatProperty scaleAnimation;
	protected VariableAnimatedColorProperty colorAnimation;
	
	protected int frameCount;
	protected boolean animateSprite;
	protected IntProperty initialFrame;
	
	public static final FixedFloatProperty DEFAULT_LIFETIME = new FixedFloatProperty(5f);
	
	public ParticlePrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public ParticlePrefab(TextureBase texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticlePrefab(String texturePath, FloatProperty lifetime) {
		this(AssetPools.textures.load(texturePath), lifetime);
	}
	
	public ParticlePrefab(TextureBase texture, FloatProperty lifetime) {
		super(texture);
		this.lifetime = lifetime;
		frameCount = ParticleRenderer.DEFAULT_FRAME_COUNT;
		animateSprite = ParticleRenderer.ANIMATE_SPRITE_DEFAULT;
		applyTimeScale = ParticleRenderer.APPLY_TIME_SCALE_DEFAULT;
	}
	
	@Override
	public void setColor(Color color) {
		setColor(new FixedColorProperty(color));
	}
	
	public void setColor(ColorProperty color) {
		this.color = color;
		colorAnimation = null;
	}
	
	/**
	 * Adds an animation for the alpha value of the particle's color, going from <code>1f</code> to <code>0f</code>.
	 */
	public void setColorAlphaAnimation(Color base, EasingFunction easingFunction) {
		setColorAlphaAnimation(base, new FixedFloatProperty(1f), new FixedFloatProperty(0f), easingFunction);
	}
	
	/**
	 * Adds a linear animation for the alpha value of the particle's color.
	 */
	public void setColorAlphaAnimation(Color base, FloatProperty alphaStart, FloatProperty alphaEnd) {
		setColorAlphaAnimation(base, alphaStart, alphaEnd, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the alpha value of the particle's color.
	 */
	public void setColorAlphaAnimation(Color base, FloatProperty alphaStart, FloatProperty alphaEnd, EasingFunction easingFunction) {
		VariableColorProperty start = new VariableColorProperty(base);
		VariableColorProperty end = new VariableColorProperty(base);
		start.setAlpha(alphaStart);
		end.setAlpha(alphaEnd);
		
		setColorAnimation(start, end, easingFunction);
	}
	
	/**
	 * Adds a linear animation for the color of the particle.
	 */
	public void setColorAnimation(ColorProperty start, ColorProperty end) {
		setColorAnimation(start, end, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the color of the particle.
	 */
	public void setColorAnimation(ColorProperty start, ColorProperty end, EasingFunction easingFunction) {
		colorAnimation = new VariableAnimatedColorProperty(start, end, new AnimationCurve(1, easingFunction));
		color = null;
	}
	
	@Override
	public void setScale(Vector2f scale) {
		setScale(new FixedFloatProperty(scale.length()));
	}
	
	public void setScale(FloatProperty scale) {
		this.scale = scale;
		scaleAnimation = null;
	}
	
	/**
	 * Adds a linear animation for the scale of the particle.
	 */
	public void setScaleAnimation(FloatProperty start, FloatProperty end) {
		setScaleAnimation(start, end, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the scale of the particle.
	 */
	public void setScaleAnimation(FloatProperty start, FloatProperty end, EasingFunction easingFunction) {
		scaleAnimation = new VariableAnimatedFloatProperty(start, end, new AnimationCurve(1, easingFunction));
		scale = null;
	}
	
	public void setLifetime(FloatProperty lifetime) {
		this.lifetime = lifetime;
	}
	
	public void setVelocity(Vector2fProperty velocity) {
		this.velocity = velocity;
	}
	
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
	
	public void setFrame(IntProperty frame) {
		setAnimateSprite(false);
		setInitialFrame(frame);
	}
	
	public void setAnimateSprite(boolean animateSprite) {
		this.animateSprite = animateSprite;
	}
	
	public void setInitialFrame(IntProperty initialFrame) {
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
			spriteRenderer.scale.x = scale.get();
			spriteRenderer.scale.y = scale.get();
		} else if (scaleAnimation != null) {
			particleRenderer.scaleAnimation = scaleAnimation.getValue();
		}
		
		// Apply color
		if (color != null) {
			color.transmit(spriteRenderer.color);
		} else if (colorAnimation != null) {
			particleRenderer.colorAnimation = colorAnimation.getValue();
		}
		
		// Apply velocity
		if (velocity != null) {
			particleRenderer.velocity = velocity.getValue();
		}
		
		// Apply initial frame
		if (initialFrame != null) {
			particleRenderer.initialFrame = initialFrame.get();
		}
		
		entity.addComponent(particleRenderer);
	}
}
