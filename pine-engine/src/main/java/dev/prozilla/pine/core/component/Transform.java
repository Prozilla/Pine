package dev.prozilla.pine.core.component;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.entity.Entity;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Transform extends Component {
	
	/** Local position */
	public Vector3f position;
	/** Rotation in degrees */
	public Vector3f rotation;
	public Vector3f scale;
	/** The velocity vector is added to the position each frame. */
	public Vector3f velocity;
	
	/** Children of the entity */
	public final List<Transform> children;
	/** Parent of the entity */
	public Transform parent;
	
	/** Z-index in the world, highest values are rendered first. */
	private int depthIndex;
	/** If true, sets the depth of children to a lower value than the parent. */
	private boolean renderChildrenBelow;
	
	private final Matrix4f modelMatrix;

	public Transform() {
		this(0, 0, 0);
	}
	
	public Transform(float x, float y, float z) {
		this(new Vector3f(x, y, z));
	}
	
	public Transform(Vector3f position) {
		this(position, new Vector3f());
	}
	
	public Transform(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
		scale = Vector3f.one();
		modelMatrix = new Matrix4f();
		
		children = new ArrayList<>();
		velocity = new Vector3f();
		
		depthIndex = 0;
		renderChildrenBelow = false;
	}
	
	@Override
	public Entity getFirstChild() {
		if (children.isEmpty()) {
			return null;
		}
		
		return children.getFirst().getEntity();
	}
	
	@Override
	public Entity getLastChild() {
		if (children.isEmpty()) {
			return null;
		}
		
		return children.getLast().getEntity();
	}
	
	@Override
	public Entity getChild(int i) {
		if (i < 0 || i >= children.size()) {
			return null;
		}
		return children.get(i).getEntity();
	}
	
	@Override
	public boolean isDescendantOf(Transform parent) {
		Checks.isNotNull(parent, "parent");
		return this.parent != null && (this.parent.equals(parent) || this.parent.isDescendantOf(parent));
	}
	
	@Override
	public Entity getChildWithTag(String tag) {
		Checks.isNotNull(tag, "tag");
		
		for (Transform child : children) {
			if (child.entity.hasTag(tag)) {
				return child.entity;
			}
		}
		
		return null;
	}
	
	@Override
	public Entity getParentWithTag(String tag) {
		Checks.isNotNull(tag, "tag");
		
		if (parent != null && parent.getEntity().hasTag(tag)) {
			return parent.entity;
		}
		
		return null;
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return getComponentInParent(componentClass, true);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		if (parent == null) {
			return null;
		}
		
		ComponentType component = parent.getComponent(componentClass);
		
		if (component == null && includeAncestors) {
			return parent.getComponentInParent(componentClass);
		}
		
		return component;
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		if (children.isEmpty()) {
			return new ArrayList<>();
		}
		
		ArrayList<ComponentType> components = new ArrayList<>();
		
		for (Transform child : children) {
			ComponentType component = child.getComponent(componentClass);
			if (component != null) {
				components.add(component);
			}
		}
		
		return components;
	}
	
	public float getGlobalX() {
		if (parent == null) {
			return position.x;
		} else {
			return position.x + parent.getGlobalX();
		}
	}
	
	public float getGlobalY() {
		if (parent == null) {
			return position.y;
		} else {
			return position.y + parent.getGlobalY();
		}
	}
	
	public Matrix4f getModelMatrix() {
		return modelMatrix.identity().translate(position.x, position.y, position.z)
			.rotateX((float)Math.toRadians(-rotation.x))
			.rotateY((float)Math.toRadians(-rotation.y))
			.rotateZ((float)Math.toRadians(-rotation.z))
			.scale(scale.x, scale.y, scale.z);
	}
	
	public void setParent(Transform parent) {
		if (Objects.equals(parent, this.parent)) {
			return;
		}
		
		this.parent = parent;
		
		if (parent != null) {
			// Temporarily borrow depth index from parent until depth is recalculated
			depthIndex = parent.depthIndex;
		}
		
		entity.invoke(Entity.EventType.PARENT_UPDATE);
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	public void translate(Vector3f delta) {
		Checks.isNotNull(delta, "delta");
		translate(delta.x, delta.y, delta.z);
	}
	
	public void translate(float deltaX, float deltaY, float deltaZ) {
		position.add(deltaX, deltaY, deltaZ);
	}
	
	public void setPosition(Vector3f position) {
		Checks.isNotNull(position, "position");
		setPosition(position.x, position.y, position.z);
	}
	
	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}
	
	public void rotate(Vector3f delta) {
		Checks.isNotNull(delta, "delta");
		rotate(delta.x, delta.y, delta.z);
	}
	
	public void rotate(float deltaX, float deltaY, float deltaZ) {
		rotation.add(deltaX, deltaY, deltaZ);
	}
	
	public void setRotation(Vector3f rotation) {
		Checks.isNotNull(rotation, "rotation");
		setRotation(rotation.x, rotation.y, rotation.z);
	}
	
	public void setRotation(float x, float y, float z) {
		rotation.set(x, y, z);
	}
	
	public void setVelocity(Vector3f velocity) {
		Checks.isNotNull(velocity, "velocity");
		setVelocity(velocity.x, velocity.y, velocity.z);
	}
	
	public void setVelocity(float x, float y, float z) {
		velocity.x = x;
		velocity.y = y;
		velocity.z = z;
	}
	
	public void setRenderChildrenBelow(boolean renderChildrenBelow) {
		if (this.renderChildrenBelow == renderChildrenBelow) {
			return;
		}
		
		this.renderChildrenBelow = renderChildrenBelow;
		getWorld().calculateDepth();
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
	
	/**
	 * @return Depth value between <code>0f</code> and <code>1f</code> based on the depth index.
	 */
	public float getDepth() {
		return ((float)depthIndex / getWorld().maxDepth);
	}
}
