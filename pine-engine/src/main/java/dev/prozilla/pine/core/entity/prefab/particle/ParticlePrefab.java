package dev.prozilla.pine.core.entity.prefab.particle;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.easing.EasingFunction;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.property.FixedProperty;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.common.property.VariableColorProperty;
import dev.prozilla.pine.common.property.animated.AnimationCurve;
import dev.prozilla.pine.common.property.animated.variable.VariableAnimatedColorProperty;
import dev.prozilla.pine.common.property.animated.variable.VariableAnimatedFloatProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.particle.ParticleRenderer;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;

public class ParticlePrefab extends SpritePrefab {
	
	protected Property<Color> color;
	protected Property<Float> lifetime;
	protected Property<Vector2f> velocity;
	protected Property<Float> scale;
	protected boolean applyTimeScale;
	
	protected VariableAnimatedFloatProperty scaleAnimation;
	protected VariableAnimatedColorProperty colorAnimation;
	
	protected int frameCount;
	protected boolean animateSprite;
	protected Property<Integer> initialFrame;
	
	public static final Property<Float> DEFAULT_LIFETIME = new FixedProperty<>(5f);
	
	public ParticlePrefab(String texturePath) {
		this(AssetPools.textures.load(texturePath));
	}
	
	public ParticlePrefab(TextureBase texture) {
		this(texture, DEFAULT_LIFETIME);
	}
	
	public ParticlePrefab(String texturePath, Property<Float> lifetime) {
		this(AssetPools.textures.load(texturePath), lifetime);
	}
	
	public ParticlePrefab(TextureBase texture, Property<Float> lifetime) {
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
	
	public void setColor(Property<Color> color) {
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
	public void setColorAlphaAnimation(Color base, Property<Float> alphaStart, Property<Float> alphaEnd) {
		setColorAlphaAnimation(base, alphaStart, alphaEnd, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the alpha value of the particle's color.
	 */
	public void setColorAlphaAnimation(Color base, Property<Float> alphaStart, Property<Float> alphaEnd, EasingFunction easingFunction) {
		VariableColorProperty start = new VariableColorProperty(base);
		VariableColorProperty end = new VariableColorProperty(base);
		start.setAlpha(alphaStart);
		end.setAlpha(alphaEnd);
		
		setColorAnimation(start, end, easingFunction);
	}
	
	/**
	 * Adds a linear animation for the color of the particle.
	 */
	public void setColorAnimation(Property<Color> start, Property<Color> end) {
		setColorAnimation(start, end, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the color of the particle.
	 */
	public void setColorAnimation(Property<Color> start, Property<Color> end, EasingFunction easingFunction) {
		colorAnimation = new VariableAnimatedColorProperty(start, end, new AnimationCurve(1, easingFunction));
		color = null;
	}
	
	@Override
	public void setScale(Vector2f scale) {
		setScale(new FixedProperty<>(scale.length()));
	}
	
	public void setScale(Property<Float> scale) {
		this.scale = scale;
		scaleAnimation = null;
	}
	
	/**
	 * Adds a linear animation for the scale of the particle.
	 */
	public void setScaleAnimation(Property<Float> start, Property<Float> end) {
		setScaleAnimation(start, end, AnimationCurve.DEFAULT_EASING_FUNCTION);
	}
	
	/**
	 * Adds an animation for the scale of the particle.
	 */
	public void setScaleAnimation(Property<Float> start, Property<Float> end, EasingFunction easingFunction) {
		scaleAnimation = new VariableAnimatedFloatProperty(start, end, new AnimationCurve(1, easingFunction));
		scale = null;
	}
	
	public void setLifetime(Property<Float> lifetime) {
		this.lifetime = lifetime;
	}
	
	public void setVelocity(Property<Vector2f> velocity) {
		this.velocity = velocity;
	}
	
	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}
	
	public void setFrame(Property<Integer> frame) {
		setAnimateSprite(false);
		setInitialFrame(frame);
	}
	
	public void setAnimateSprite(boolean animateSprite) {
		this.animateSprite = animateSprite;
	}
	
	public void setInitialFrame(Property<Integer> initialFrame) {
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
			spriteRenderer.scale.x = scale.getValue();
			spriteRenderer.scale.y = scale.getValue();
		} else if (scaleAnimation != null) {
			particleRenderer.scaleAnimation = scaleAnimation.getValue();
		}
		
		// Apply color
		if (color != null) {
			spriteRenderer.color.receive(color.getValue());
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
