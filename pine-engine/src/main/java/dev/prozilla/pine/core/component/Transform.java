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
	public <ComponentType extends Component> ComponentType getComponentAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf) {
		if (includeSelf) {
			ComponentType component = getComponent(componentClass);
			if (component != null) {
				return component;
			}
		}
		
		if (parent == null) {
			return null;
		}
		
		ComponentType component = parent.getComponent(componentClass);
		
		if (component == null && includeGrandParents) {
			return parent.getComponentAbove(componentClass);
		}
		
		return component;
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponentsAbove(Class<ComponentType> componentClass, boolean includeGrandParents, boolean includeSelf) {
		ArrayList<ComponentType> components = new ArrayList<>();
		
		Transform currentParent = includeSelf ? this : parent;
		while (currentParent != null) {
			ComponentType component = currentParent.getComponent(componentClass);
			if (component != null) {
				components.add(component);
			}
			if (currentParent == this || includeGrandParents) {
				currentParent = currentParent.parent;
			} else {
				currentParent = null;
			}
		}
		
		return components;
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponentsBelow(Class<ComponentType> componentClass, boolean includeGrandChildren, boolean includeSelf, boolean includeNested) {
		ArrayList<ComponentType> components = new ArrayList<>();
		
		if (includeSelf) {
			ComponentType component = getComponent(componentClass);
			if (component != null) {
				components.add(component);
				if (!includeNested) {
					return components;
				}
			}
		}

		if (children.isEmpty()) {
			return components;
		}
		
		for (Transform child : children) {
			ComponentType component = child.getComponent(componentClass);
			if (component != null) {
				components.add(component);
			}
			if (includeGrandChildren) {
				components.addAll(child.getComponentsBelow(componentClass, component == null || includeNested, false));
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
	
	public Vector3f getForward() {
		float pitch = getPitch();
		float yaw = getYaw();
		float cosY = (float)Math.cos(yaw);
		float sinY = (float)Math.sin(yaw);
		float cosP = (float)Math.cos(pitch);
		float sinP = (float)Math.sin(pitch);
		
		return new Vector3f(sinY * cosP, -sinP, -cosY * cosP);
	}
	
	public Vector3f getRight() {
		float yaw = getYaw();
		return new Vector3f((float)Math.cos(yaw), 0, (float)Math.sin(yaw));
	}
	
	public Vector3f getUp() {
		float pitch = getPitch();
		float yaw = getYaw();
		float cosY  = (float)Math.cos(yaw);
		float sinY  = (float)Math.sin(yaw);
		float cosP  = (float)Math.cos(pitch);
		float sinP  = (float)Math.sin(pitch);
		
		return new Vector3f(sinY * sinP, cosP, -cosY * sinP);
	}
	
	public Matrix4f getModelMatrix() {
		return modelMatrix.identity().translate(position.x, position.y, position.z)
			.rotateX(-getPitch())
			.rotateY(-getYaw())
			.rotateZ(-getRoll())
			.scale(scale.x, scale.y, scale.z);
	}
	
	public float getPitch() {
		return (float)Math.toRadians(rotation.x);
	}
	
	public float getYaw() {
		return (float)Math.toRadians(rotation.y);
	}
	
	public float getRoll() {
		return (float)Math.toRadians(rotation.z);
	}
	
	public void setParent(Transform parent) {
		if (Objects.equals(parent, this.parent)) {
			return;
		}
		
		this.parent = parent;
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
}
