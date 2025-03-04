package dev.prozilla.pine.examples.snake.component;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.particle.ParticleBurstEmitter;
import dev.prozilla.pine.core.component.sprite.TileRenderer;

public class AppleData extends Component {
	
	public ParticleBurstEmitter particleEmitter;
	
	public void eat() {
		TileRenderer tileRenderer = getComponent(TileRenderer.class);
		Vector2f position = getTransform().position;
		
		particleEmitter.emit(position.x + tileRenderer.size / 2f, position.y + tileRenderer.size / 2f);
		tileRenderer.remove();
	}
	
}
