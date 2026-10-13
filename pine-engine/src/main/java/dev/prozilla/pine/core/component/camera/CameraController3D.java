package dev.prozilla.pine.core.component.camera;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.input.InputBinding;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.state.input.Key;

public class CameraController3D extends Component {
	
	public float translationSpeed;
	public float rotationSpeed;
	public float velocityDamping;
	public boolean disableControlsOnPause;
	
	public final Vector3f target;
	public final Vector2i previousCursorPosition;
	
	public final InputBinding forward;
	public final InputBinding backwards;
	public final InputBinding left;
	public final InputBinding right;
	public final InputBinding up;
	public final InputBinding down;
	public final InputBinding speedUp;
	public final InputBinding slowDown;
	
	public static final float DEFAULT_DAMPING = 0f;
	
	public CameraController3D() {
		target = new Vector3f();
		previousCursorPosition = new Vector2i();
	
		forward = new InputBinding(Key.W);
		backwards = new InputBinding(Key.S);
		left = new InputBinding(Key.A);
		right = new InputBinding(Key.D);
		up = new InputBinding(Key.E);
		down = new InputBinding(Key.Q);
		speedUp = new InputBinding(Key.L_SHIFT);
		slowDown = new InputBinding(Key.R_SHIFT);
	}
}
