package dev.prozilla.pine.core.component.light;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Component;

public class SunLight extends Component {
	
	public Color color;
	public float intensity;
	
	public SunLight(Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	public Vector3f getDirection() {
		return getTransform().getUp();
	}
	
}
