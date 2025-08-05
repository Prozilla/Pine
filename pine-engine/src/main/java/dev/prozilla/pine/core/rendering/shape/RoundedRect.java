package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.rendering.shape.modifier.BevelModifier;

public class RoundedRect extends Rect {
	
	private final BevelModifier bevelModifier;
	
	public RoundedRect(Vector2f position, Vector2f size) {
		this(position, size, Math.min(size.x, size.y));
	}
	
	public RoundedRect(Vector2f position, Vector2f size, float roundness) {
		super(position, size);
		bevelModifier = new BevelModifier(roundness);
		addModifier(bevelModifier);
	}
	
	public void setRoundness(float roundness) {
		bevelModifier.setBevelAmount(roundness);
	}
	
	public void setSegments(int segments) {
		bevelModifier.setSegments(segments);
	}
	
	public BevelModifier getBevelModifier() {
		return bevelModifier;
	}
	
}
