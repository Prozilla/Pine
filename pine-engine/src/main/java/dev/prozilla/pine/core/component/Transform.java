package dev.prozilla.pine.core.component;

import java.util.ArrayList;
import java.util.List;

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
	public final List<Transform> children;
	/** Parent of the entity */
	public Transform parent;
	
	/** Z-index in the world, highest values are rendered first. */
	private int depthIndex;
	/** If true, sets the depth of children to a lower value than the parent. */
	public boolean renderChildrenBelow;

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
		
		depthIndex = 0;
		renderChildrenBelow = false;
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
	
	/**
	 * Calculates the z-indices of this transform and its children based on a depth value.
	 * @param depth Depth value before calculation
	 * @return Depth value after calculation
	 */
	public int calculateDepth(int depth) {
		if (!renderChildrenBelow) {
			depthIndex = depth++;
		}
		
		for (Transform child : children) {
			depth = child.calculateDepth(depth);
		}
		
		if (renderChildrenBelow) {
			depthIndex = depth++;
		}
		
		return depth;
	}
	
	public int getDepthIndex() {
		return depthIndex;
	}
	
	public float getDepth() {
		return ((float)depthIndex / getWorld().maxDepth);
	}
}
