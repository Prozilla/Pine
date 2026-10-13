package dev.prozilla.pine.core.entity.prefab.camera;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.camera.CameraController3D;
import dev.prozilla.pine.core.component.camera.CameraData;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Components;

@Components({ CameraController3D.class, CameraData.class, Transform.class })
public class Controllable3DCameraPrefab extends CameraPrefab {
	
	protected float translationSpeed;
	protected float rotationSpeed;
	protected float velocityDamping;
	protected boolean disableControlsOnPause;
	
	public Controllable3DCameraPrefab(float translationSpeed, float rotationSpeed) {
		this(translationSpeed, rotationSpeed, CameraController3D.DEFAULT_DAMPING);
	}
	
	public Controllable3DCameraPrefab(float translationSpeed, float rotationSpeed, float velocityDamping) {
		this.translationSpeed = translationSpeed;
		this.rotationSpeed = rotationSpeed;
		this.velocityDamping = velocityDamping;
		
		disableControlsOnPause = false;
		
		setName("ControllableCamera");
	}
	
	public void disableTranslation() {
		setTranslationSpeed(0);
	}
	
	public void setTranslationSpeed(float translationSpeed) {
		this.translationSpeed = translationSpeed;
	}
	
	public void disableVelocityDamping() {
		setVelocityDamping(0);
	}
	
	public void setVelocityDamping(float velocityDamping) {
		this.velocityDamping = velocityDamping;
	}
	
	public void setDisableControlsOnPause(boolean disableControlsOnPause) {
		this.disableControlsOnPause = disableControlsOnPause;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		CameraController3D cameraControlData = entity.addComponent(new CameraController3D());
		
		cameraControlData.translationSpeed = translationSpeed;
		cameraControlData.rotationSpeed = rotationSpeed;
		cameraControlData.velocityDamping = velocityDamping;
		cameraControlData.disableControlsOnPause = disableControlsOnPause;
	}
}
