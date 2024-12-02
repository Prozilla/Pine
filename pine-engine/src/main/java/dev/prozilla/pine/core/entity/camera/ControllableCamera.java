package dev.prozilla.pine.core.entity.camera;

import dev.prozilla.pine.core.Game;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.core.state.input.KeyBindings;

/**
 * Camera object that can be moved and zoomed.
 */
public class ControllableCamera extends Camera {
	
	protected float movementSpeed;
	protected float velocityDamping;
	
	protected float zoomSpeed;
	protected float minZoom;
	protected float maxZoom;
	
	protected float targetX;
	protected float targetY;
	protected float targetZoom;
	
	protected boolean enableBounds;
	protected float boundsX;
	protected float boundsY;
	protected float boundsWidth;
	protected float boundsHeight;
	
	public enum Action {
		FORWARD,
		BACKWARDS,
		RIGHT,
		LEFT,
		ZOOM_IN,
		ZOOM_OUT,
		SPEED_UP,
		SLOW_DOWN,
	}
	
	protected final KeyBindings<Action> keyBindings;
	
	/**
	 * Creates a controllable camera.
	 * @param game Reference to the game
	 * @param movementSpeed Movement speed (Set to <code>0</code> to disable movement)
	 * @param velocityDamping Velocity damping (Set to <code>0</code> to disable damping)
	 * @param zoomSpeed Zoom speed (Set to <code>0</code> to disable zooming)
	 * @param minZoom Minimum zoom factor
	 * @param maxZoom Maximum zoom factor
	 */
	public ControllableCamera(Game game, float movementSpeed, float velocityDamping, float zoomSpeed, float minZoom, float maxZoom) {
		super(game);
		
		this.movementSpeed = movementSpeed;
		this.velocityDamping = velocityDamping;
		
		this.zoomSpeed = zoomSpeed;
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		
		enableBounds = false;
		
		keyBindings = new KeyBindings<>(getInput());
		
		keyBindings.addAction(Action.FORWARD, new Key[] { Key.UP_ARROW, Key.W });
		keyBindings.addAction(Action.BACKWARDS, new Key[] { Key.DOWN_ARROW, Key.S });
		keyBindings.addAction(Action.RIGHT, new Key[] { Key.RIGHT_ARROW, Key.D });
		keyBindings.addAction(Action.LEFT, new Key[] { Key.LEFT_ARROW, Key.A });
		keyBindings.addAction(Action.ZOOM_IN, Key.X);
		keyBindings.addAction(Action.ZOOM_OUT, Key.Z);
		keyBindings.addAction(Action.SPEED_UP, Key.L_SHIFT);
		keyBindings.addAction(Action.SLOW_DOWN, Key.L_CONTROL);
	}
	
	/**
	 * Resets the camera target position and target zoom.
	 */
	@Override
	public void init(long window) {
		super.init(window);
		
		targetX = x;
		targetY = y;
		targetZoom = zoomFactor;
	}
	
	/**
	 * Handle input for camera movement and zooming.
	 */
	@Override
	public void input(float deltaTime) {
		super.input(deltaTime);
		handleMovementInput(deltaTime);
		handleZoomInput(deltaTime);
	}
	
	/**
	 * Updates the camera's position and zoom factor based on targets and velocity.
	 */
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if (velocityDamping == 0) {
			x = targetX;
			y = targetY;
			zoomFactor = targetZoom;
		} else {
			// Calculate velocity towards the target position and zoom with damping
			float velocityX = (targetX - x) * velocityDamping;
			float velocityY = (targetY - y) * velocityDamping;
			float velocityZoom = (targetZoom - zoomFactor) * velocityDamping;
			
			// Apply velocities to update position and zoom smoothly
			x += velocityX;
			y += velocityY;
			zoomFactor += velocityZoom;
		}
		
		// Clamp camera zoom to the allowed ranges
		zoomFactor = MathUtils.clamp(zoomFactor, minZoom, maxZoom);
		
		// Clamp camera position and target position to bounds
		if (enableBounds) {
			x = MathUtils.clamp(x, getMinX(), getMaxX());
			y = MathUtils.clamp(y, getMinY(), getMaxY());
			targetX = MathUtils.clamp(targetX, getMinX(), getMaxX());
			targetY = MathUtils.clamp(targetY, getMinY(), getMaxY());
		}
	}
	
	/**
	 * Handle camera movement input.
	 * @param deltaTime Delta time in seconds
	 */
	void handleMovementInput(float deltaTime) {
		if (movementSpeed == 0)
			return;
		
		float deltaX = 0f, deltaY = 0f;
		
		// Handle movement by key presses
		if (keyBindings.isActive(Action.RIGHT))
			deltaX += 1f;
		if (keyBindings.isActive(Action.LEFT))
			deltaX -= 1f;
		if (keyBindings.isActive(Action.FORWARD))
			deltaY += 1f;
		if (keyBindings.isActive(Action.BACKWARDS))
			deltaY -= 1f;
		
		float movementFactor = (movementSpeed / (zoomFactor * zoomFactor)) * deltaTime;
		
		// Apply speed modifiers
		if (keyBindings.isActive(Action.SPEED_UP)) {
			movementFactor *= 2f;
		} else if (keyBindings.isActive(Action.SLOW_DOWN)) {
			movementFactor *= 0.5f;
		}
		
		deltaX *= movementFactor;
		deltaY *= movementFactor;
		
		targetX = targetX + deltaX;
		targetY = targetY + deltaY;
	}
	
	/**
	 * Handle camera zoom input.
	 * @param deltaTime Delta time in seconds
	 */
	void handleZoomInput(float deltaTime) {
		if (zoomSpeed == 0)
			return;
		
		float deltaZoom = 0f;
		
		// Handle zooming by key presses
		if (keyBindings.isActive(Action.ZOOM_IN))
			deltaZoom += 0.01f;
		if (keyBindings.isActive(Action.ZOOM_OUT))
			deltaZoom -= 0.01f;
		
		// Handle zooming by scrolling
		if (getInput().getScrollY() < 0) {
			deltaZoom -= 0.1f;
		} else if (getInput().getScrollY() > 0) {
			deltaZoom += 0.1f;
		}
		
		deltaZoom = deltaZoom * zoomSpeed * deltaTime;
		
		// Apply speed modifiers
		if (keyBindings.isActive(Action.SPEED_UP)) {
			deltaZoom *= 2f;
		} else if (keyBindings.isActive(Action.SLOW_DOWN)) {
			deltaZoom *= 0.5f;
		}
		
		targetZoom = MathUtils.clamp(targetZoom + deltaZoom, minZoom, maxZoom);
	}
	
	/**
	 * Returns the minimum X position for the camera.
	 * @return X position
	 */
	protected float getMinX() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsX;
	}
	
	/**
	 * Returns the maximum X position for the camera.
	 * @return X position
	 */
	protected float getMaxX() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsX + boundsWidth / 2f;
	}
	
	/**
	 * Returns the minimum Y position for the camera.
	 * @return Y position
	 */
	protected float getMinY() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsY;
	}
	
	/**
	 * Returns the maximum Y position for the camera.
	 * @return Y position
	 */
	protected float getMaxY() {
		if (!enableBounds) {
			return 0;
		}
		
		return boundsY + boundsHeight / 2f;
	}
	
	/**
	 * Sets the bounds of this camera.
	 */
	public void setBounds(float x, float y, float width, float height) {
		enableBounds = true;
		boundsX = x;
		boundsY = y;
		boundsWidth = width;
		boundsHeight = height;
	}
}
