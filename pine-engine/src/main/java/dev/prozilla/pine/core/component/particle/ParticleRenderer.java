package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Component;

public class ParticleRenderer extends Component {
	
	public float initialLifetime;
	public float lifetime;
	public Vector2f velocity;
	
	public int frameCount;
	public boolean animateSprite;
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
