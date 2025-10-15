package dev.prozilla.pine.core.entity.prefab.camera;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;
import dev.prozilla.pine.core.entity.prefab.Prefab;

@Components({ CameraData.class, Transform.class })
public class CameraPrefab extends Prefab {
	
	protected Color backgroundColor;
	
	public CameraPrefab() {
		this(null);
	}
	
	public CameraPrefab(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		setName("Camera");
	}
	
	public void setBackgroundColor(Color color) {
		backgroundColor = color;
	}
	
	@Override
	protected void apply(Entity entity) {
		CameraData cameraData = entity.addComponent(new CameraData());
		
		if (backgroundColor != null) {
			cameraData.backgroundColor = backgroundColor;
		}
	}
}
