package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.math.matrix.Matrix4f;

public class OverlayCameraData extends CameraData {
	
	public OverlayCameraData() {
		orthographic = true;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix.setIdentity().translate(0, 0, -farClipPlane);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix.setIdentity().orthographic(0, width, 0, height, nearClipPlane, farClipPlane);
	}
}
