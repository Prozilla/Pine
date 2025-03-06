package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Component;

public class ParticleRenderer extends Component {
	
	public float initialLifetime;
	/**
	 * Remaining life of this particle, in seconds.
	 */
	public float lifetime;
	/**
	 * Current velocity vector.
	 */
	public Vector2f velocity;
	
	/**
	 * Amount of frames in the texture of this particle.
	 */
	public int frameCount;
	/**
	 * Whether to animate the sprite of this particle depending on its remaining lifetime.
	 */
	public boolean animateSprite;
	/**
	 * Initial frame of the sprite of this particle.
	 */
	public int initialFrame;
	
	public static final int DEFAULT_FRAME_COUNT = 1;
	public static final boolean ANIMATE_SPRITE_DEFAULT = true;
	public static final int DEFAULT_INITIAL_FRAME = 0;
	
	public ParticleRenderer(float lifetime) {
		this.lifetime = lifetime;
		initialLifetime = lifetime;
		frameCount = DEFAULT_FRAME_COUNT;
		animateSprite = ANIMATE_SPRITE_DEFAULT;
		initialFrame = DEFAULT_INITIAL_FRAME;
	}
	
	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
		initialLifetime = lifetime;
	}
	
	public void kill() {
		entity.destroy();
	}
	
}
