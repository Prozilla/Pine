package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.system.resource.Color;
import dev.prozilla.pine.core.component.Component;

public class CameraData extends Component {
	
	public float zoomFactor;
	
	public float width;
	public float height;
	
	public Color backgroundColor;
	
	public CameraData() {
		this(Color.BLACK.clone());
	}
	
	public CameraData(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		
		zoomFactor = 1;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
