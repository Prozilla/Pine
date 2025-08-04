package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.rendering.shape.Rect;

public class BarRect extends Rect {
	
	public BarRect() {
		super(new Vector2f(), new Vector2f());
	}
	
	public void updateHeight(float height, float deltaTime) {
		setHeight(MathUtils.lerp(getHeight(), height * 64f, deltaTime * Main.LERP_SPEED));
	}
	
}
