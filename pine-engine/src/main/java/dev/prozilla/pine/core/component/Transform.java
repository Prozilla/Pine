package dev.prozilla.pine.core.component;

import java.util.ArrayList;

public class Transform extends Component {
	
	/** Local X position value */
	public float x;
	/** Local Y position value */
	public float y;
	/** Rotation in degrees */
	public float rotation;
	/** Horizontal velocity, X position is increased by this value every frame. */
	public float velocityX;
	/** Vertical velocity, Y position is increased by this value every frame. */
	public float velocityY;
	
	/** Children of the entity */
	public final ArrayList<Transform> children;
	/** Parent of the entity */
	public Transform parent;

	public Transform() {
		this(0, 0);
	}
	
	public Transform(float x, float y) {
		this(x, y, 0);
	}
	
	public Transform(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		
		children = new ArrayList<>();
		velocityX = 0;
		velocityY = 0;
	}
	
	public float getGlobalX() {
		if (parent == null) {
			return x;
		} else {
			return x + parent.getGlobalX();
		}
	}
	
	public float getGlobalY() {
		if (parent == null) {
			return y;
		} else {
			return y + parent.getGlobalY();
		}
	}
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public void translate(float deltaX, float deltaY) {
		x += deltaX;
		y += deltaY;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVelocity(float x, float y) {
		velocityX = x;
		velocityY = y;
	}
}
