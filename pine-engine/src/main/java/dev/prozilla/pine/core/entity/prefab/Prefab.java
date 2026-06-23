package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.adaptive.AdaptiveVector3fProperty;
import dev.prozilla.pine.common.property.vector.Vector3fProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.animation.AnimationData;
import dev.prozilla.pine.core.component.driver.TransformDriver;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.Scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class representing a prefab for creating entities with predefined components and values.
 */
@Components({ Transform.class })
public class Prefab {
	
	protected String name;
	protected String tag;
	protected boolean isActive = true;
	protected final List<Prefab> children;
	
	protected AdaptiveVector3fProperty positionProperty;
	protected AdaptiveVector3fProperty rotationProperty;
	protected AdaptiveVector3fProperty scaleProperty;
	
	public Prefab() {
		children = new ArrayList<>();
		positionProperty = AdaptiveVector3fProperty.adapt(new Vector3f());
		rotationProperty = AdaptiveVector3fProperty.adapt(new Vector3f());
		scaleProperty = AdaptiveVector3fProperty.adapt(Vector3f.one());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public void setActive(boolean active) {
		isActive = active;
	}
	
	public void addChildren(Prefab... children) {
		addChildren(List.of(children));
	}
	
	public void addChildren(Collection<Prefab> children) {
		this.children.addAll(children);
	}
	
	public void addChild(Prefab child) {
		Checks.isNotNull(child, "child");
		children.add(child);
	}
	
	public void removeChild(Prefab child) {
		children.remove(child);
	}
	
	public void setPosition(Vector3f position) {
		positionProperty = AdaptiveVector3fProperty.adapt(position);
	}
	
	public void setPosition(Vector3fProperty position) {
		positionProperty = AdaptiveVector3fProperty.adapt(position);
	}
	
	public void setRotation(Vector3f rotation) {
		rotationProperty = AdaptiveVector3fProperty.adapt(rotation);
	}
	
	public void setRotation(Vector3fProperty rotation) {
		rotationProperty = AdaptiveVector3fProperty.adapt(rotation);
	}
	
	public void setScale(Vector3f scale) {
		scaleProperty = AdaptiveVector3fProperty.adapt(scale);
	}
	
	public void setScale(Vector3fProperty scale) {
		scaleProperty = AdaptiveVector3fProperty.adapt(scale);
	}
	
	public Entity instantiate(Scene scene, Vector3f position) {
		Checks.isNotNull(position, "position");
		return instantiate(scene, position.x, position.y, position.z);
	}
	
	/**
	 * Creates a new entity instance with the prefab's default components.
	 * @param scene The scene to add this entity to.
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 * @return A new entity instance.
	 */
	public Entity instantiate(Scene scene, float x, float y, float z) {
		Entity entity = instantiate(scene);
		entity.transform.setPosition(x, y, z);
		return entity;
	}
	
	/**
	 * Creates a new entity instance with the prefab's default components at position (0, 0, 0).
	 * @param scene The scene to add this entity to.
	 * @return A new entity instance.
	 */
	public Entity instantiate(Scene scene) {
		Entity entity;
		if (name != null) {
			entity = new Entity(scene, name);
		} else {
			entity = new Entity(scene);
		}
		
		try {
			apply(entity);
		} catch (RuntimeException e) {
			String message = "Failed to instantiate prefab";
			if (name != null) {
				message += ": " + name;
			}
			
			scene.getLogger().error(message, e);
		}
		
		if (!isActive) {
			entity.setActive(false);
		}
		
		return entity;
	}
	
	/**
	 * Adds this prefab's predefined components to a given entity and copies values from this prefab.
	 */
	protected void apply(Entity entity) {
		if (tag != null) {
			entity.tag = tag;
		}
		
		entity.transform.setPosition(positionProperty.getValue());
		entity.transform.setRotation(rotationProperty.getValue());
		entity.transform.setScale(scaleProperty.getValue());
		if (positionProperty.isDynamic() || rotationProperty.isDynamic() || scaleProperty.isDynamic()) {
			AnimationData animationData = entity.addComponent(new AnimationData());
			TransformDriver driver = entity.addComponent(new TransformDriver(animationData));
			
			if (positionProperty.isDynamic()) {
				driver.setPositionProperty(positionProperty);
			}
			if (rotationProperty.isDynamic()) {
				driver.setRotationProperty(rotationProperty);
			}
			if (scaleProperty.isDynamic()) {
				driver.setScaleProperty(scaleProperty);
			}
		}
		
		for (Prefab child : children) {
			entity.addChild(child);
		}
	}
	
}
