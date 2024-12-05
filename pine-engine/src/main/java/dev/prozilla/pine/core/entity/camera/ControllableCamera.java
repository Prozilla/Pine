package dev.prozilla.pine.core.entity.camera;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.camera.CameraControlData;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.state.input.KeyBindings;

/**
 * Camera object that can be moved and zoomed.
 */
public class ControllableCamera extends Camera {
	
	public CameraControlData cameraControlData;
	
	/**
	 * Creates a controllable camera.
	 *
	 * @param world           Reference to the world
	 * @param movementSpeed   Movement speed (Set to <code>0</code> to disable movement)
	 * @param velocityDamping Velocity damping (Set to <code>0</code> to disable damping)
	 * @param zoomSpeed       Zoom speed (Set to <code>0</code> to disable zooming)
	 * @param minZoom         Minimum zoom factor
	 * @param maxZoom         Maximum zoom factor
	 */
	public ControllableCamera(World world, float movementSpeed, float velocityDamping, float zoomSpeed, float minZoom, float maxZoom) {
		super(world);
		
		cameraControlData = new CameraControlData();
		addComponent(cameraControlData);
		
		cameraControlData.movementSpeed = movementSpeed;
		cameraControlData.velocityDamping = velocityDamping;
		
		cameraControlData.zoomSpeed = zoomSpeed;
		cameraControlData.minZoom = minZoom;
		cameraControlData.maxZoom = maxZoom;
		
		cameraControlData.enableBounds = false;
	}
}
