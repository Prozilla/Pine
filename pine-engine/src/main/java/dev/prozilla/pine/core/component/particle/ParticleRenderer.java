package dev.prozilla.pine.core.component.particle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Component;

public class ParticleRenderer extends Component {
	
	public float initialLifetime;
	public float lifetime;
	public Vector2f velocity;
	public int frameCount;
	
	public static final int DEFAULT_FRAME_COUNT = 1;
	
	public ParticleRenderer(float lifetime) {
		this.lifetime = lifetime;
		initialLifetime = lifetime;
		frameCount = DEFAULT_FRAME_COUNT;
	}
	
	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
		initialLifetime = lifetime;
	}
	
	public void kill() {
		entity.destroy();
	}
	
}
