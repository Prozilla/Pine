package dev.prozilla.pine.core.component.camera;

import org.joml.Matrix4f;

public class OverlayCameraData extends CameraData {
	
	public OverlayCameraData() {
		orthographic = true;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix.identity().translate(0, 0, -farClipPlane);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix.setOrtho(0, width, 0, height, nearClipPlane, farClipPlane);
	}
}
