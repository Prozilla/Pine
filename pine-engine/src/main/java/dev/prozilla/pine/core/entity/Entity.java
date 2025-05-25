package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.common.event.EventDispatcher;
import dev.prozilla.pine.common.logging.Logger;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationProvider;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentsContext;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.SceneProvider;
import dev.prozilla.pine.core.scene.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a unique entity in the world with a list of components.
 */
public class Entity extends EventDispatcher<EntityEventType, Entity> implements Printable, EntityContext, ComponentsContext, ApplicationProvider, SceneProvider {
	
	public final int id;
	private final String name;
	public String tag;
	protected boolean isActive;
	
	public final Transform transform;
	
	protected final World world;
	protected final Application application;
	protected final Logger logger;
	protected final Scene scene;
	
	/** Components of this entity */
	public final List<Component> components;
	
	/**
	 * Creates an entity at the position (0, 0)
	 */
	public Entity(World world) {
		this(world, 0, 0);
	}
	
	/**
	 * Creates an entity at the position (0, 0)
	 */
	public Entity(World world, String name) {
		this(world, name, 0, 0);
	}
	
	/**
	 * Creates an entity at the position (x, y)
	 */
	public Entity(World world, float x, float y) {
		this(world, null, x, y);
	}
	
	/**
	 * Creates an entity at the position (x, y)
	 */
	public Entity(World world, String name, float x, float y) {
		this.world = Checks.isNotNull(world, "world");
		this.name = name;

		application = world.application;
		logger = application.getLogger();
		scene = world.scene;
		
		id = EntityManager.generateEntityId();
		
		transform = new Transform(x, y);
		components = new ArrayList<>();
		addComponent(transform);

		isActive = true;
	}
	
	public void destroyChildren() {
		for (Transform child : transform.children.toArray(new Transform[]{})) {
			child.getEntity().destroy();
		}
	}
	
	/**
	 * Destroys this entity at the end of the game loop.
	 */
	@Override
	public void destroy() {
		if (application.isRunning() && !application.isLoading()) {
			destroyChildren();
			
			// Remove child from parent
			if (transform.parent != null) {
				transform.parent.getEntity().removeChild(this);
			}
			
			// Unregister entity from world
			if (isRegistered()) {
				world.removeEntity(this);
			}
			
			invoke(EntityEventType.DESTROY, this);
			super.destroy();
		}
	}
	
	/**
	 * Instantiates a prefab and adds the instance as a child of this entity
	 * @param prefab Prefab for the child entity
	 * @return Child entity
	 */
	public Entity addChild(Prefab prefab) throws IllegalStateException, IllegalArgumentException {
		Checks.isNotNull(prefab, "prefab");
		return addChild(prefab.instantiate(world));
	}
	
	/**
	 * Adds a child to this entity. Also adds the child to the world if this entity is inside the world.
	 * @param child Entity to add as a child
	 * @return Child entity
	 */
	public Entity addChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		Checks.isNotNull(child, "child");
		
		if (transform.children.contains(child.transform)) {
			throw new IllegalStateException("Entity is already a child");
		}
		
		transform.children.add(child.transform);
		child.transform.setParent(transform);
		
		if (isRegistered()) {
			world.addEntity(child);
		}
		
		invoke(EntityEventType.CHILD_ADD, child);
		invoke(EntityEventType.CHILDREN_UPDATE, this);
		
		return child;
	}
	
	/**
	 * Adds children to this entity.
	 * @param children Child objects
	 */
	public void addChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			addChild(child);
		}
	}
	
	/**
	 * Detaches a child from this entity without removing it from the world.
	 * @param child Child object
	 */
	public void removeChild(Entity child) throws IllegalStateException, IllegalArgumentException {
		Checks.isNotNull(child, "child");
		
		boolean removed = transform.children.remove(child.transform);
		if (!removed) {
			throw new IllegalStateException("Entity is not a child");
		}
		
		child.transform.parent = null;
		
		invoke(EntityEventType.CHILD_REMOVE, child);
		invoke(EntityEventType.CHILDREN_UPDATE, this);
	}
	
	/**
	 * Detaches children from this entity without removing them from the world.
	 * @param children Child entities
	 */
	public void removeChildren(Entity... children) throws IllegalStateException, IllegalArgumentException {
		for (Entity child : children) {
			removeChild(child);
		}
	}
	
	public void removeAllChildren() {
		for (Transform child : transform.children) {
			removeChild(child.getEntity());
		}
	}
	
	@Override
	public Entity getFirstChild() {
		return transform.getFirstChild();
	}
	
	@Override
	public Entity getLastChild() {
		return transform.getLastChild();
	}
	
	@Override
	public boolean isDescendantOf(Transform parent) {
		return transform.isDescendantOf(parent);
	}
	
	@Override
	public Entity getChildWithTag(String tag) {
		return transform.getChildWithTag(tag);
	}
	
	@Override
	public Entity getParentWithTag(String tag) {
		return transform.getParentWithTag(tag);
	}
	
	/**
	 * Setter for the parent entity.
	 * @param parent Parent entity
	 */
	public void setParent(Entity parent) {
		Checks.isNotNull(parent, "parent");
		transform.setParent(parent.transform);
	}
	
	/**
	 * Toggles the active state of this entity.
	 */
	public void setActive(boolean active) {
		if (this.isActive == active) {
			return;
		}
		
		this.isActive = active;
		
		if (active) {
			world.activateEntity(this);
		}
	}
	
	public boolean isActive() {
		if (transform.parent == null) {
			return isActive;
		} else {
			Entity parent = transform.parent.getEntity();
			return isActive && (parent == null || parent.isActive());
		}
	}
	
	/**
	 * Adds a component to this entity.
	 * @param component Component
	 */
	public <C extends Component> C addComponent(C component) {
		Checks.isNotNull(component, "component");
		world.addComponent(this, component);
		invoke(EntityEventType.COMPONENTS_UPDATE, this);
		return component;
	}
	
	/**
	 * Removes a component from this entity.
	 * @param component Component
	 */
	public void removeComponent(Component component) {
		Checks.isNotNull(component, "component");
		if (!components.contains(component)) {
			return;
		}

		world.removeComponent(this, component);
		invoke(EntityEventType.COMPONENTS_UPDATE, this);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass) {
		return transform.getComponentInParent(componentClass);
	}
	
	@Override
	public <ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean includeAncestors) {
		return transform.getComponentInParent(componentClass, includeAncestors);
	}
	
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass) {
		return transform.getComponentsInChildren(componentClass);
	}
	
	/**
	 * Checks if this entity has a component of a given class.
	 * @param componentClass Class of the component.
	 * @return True if this entity has a component of type <code>componentClass</code>.
	 */
	public <ComponentType extends Component> boolean hasComponent(Class<ComponentType> componentClass) {
		return getComponent(componentClass) != null;
	}
	
	/**
	 * Returns a component of a given class or null of there isn't one.
	 * @param componentClass Class of the component.
	 * @return An instance of <code>componentClass</code> that is attached to this entity.
	 */
	@Override
	public <ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass) {
		if (components.isEmpty()) {
			return null;
		}

		// Find component that is an instance of componentClass
		ComponentType match = null;
		for (Component component : components) {
			if (componentClass.isInstance(component)) {
				match = componentClass.cast(component);
				break;
			}
		}
		
		return match;
	}
	
	/**
	 * Returns all components of a given class.
	 * @param componentClass Class of the components.
	 * @return An ArrayList of instance of <code>componentClass</code> that are attached to this entity.
	 */
	@Override
	public <ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass) {
		if (components.isEmpty()) {
			return new ArrayList<>();
		}
		
		// Find all components that are instances of componentClass
		ArrayList<ComponentType> matches = new ArrayList<>();
		for (Component component : components) {
			if (componentClass.isInstance(component)) {
				matches.add(componentClass.cast(component));
			}
		}
		
		return matches;
	}
	
	@Override
	public void invoke(EntityEventType entityEventType, Entity event) {
		super.invoke(entityEventType, event);
		
		switch (entityEventType) {
			case CHILD_ADD -> super.invoke(EntityEventType.DESCENDANT_ADD, event);
			case CHILD_REMOVE -> super.invoke(EntityEventType.DESCENDANT_REMOVE, event);
			case CHILDREN_UPDATE -> super.invoke(EntityEventType.DESCENDANT_UPDATE, event);
		}
		
		if (transform.parent != null) {
			Entity parent = transform.parent.getEntity();
			switch (entityEventType) {
				case DESCENDANT_ADD -> parent.invoke(EntityEventType.DESCENDANT_ADD, event);
				case DESCENDANT_REMOVE -> parent.invoke(EntityEventType.DESCENDANT_REMOVE, event);
				case DESCENDANT_UPDATE -> parent.invoke(EntityEventType.DESCENDANT_UPDATE, event);
			}
		}
	}
	
	public String getName() {
		return getName(null);
	}
	
	public String getName(String defaultName) {
		if (name != null) {
			return name;
		} else if (defaultName != null) {
			return defaultName;
		} else {
			return "Entity";
		}
	}
	
	/**
	 * Checks whether this entity has a given tag.
	 */
	public boolean hasTag(String tag) {
		if (this.tag == null) {
			return tag == null;
		}
		
		return this.tag.equals(tag);
	}
	
	@Override
	public Application getApplication() {
		return application;
	}
	
	@Override
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Checks whether this entity is registered in the entity manager.
	 */
	public boolean isRegistered() {
		return world.entityManager.contains(this);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	/**
	 * Checks whether two entities are equal.
	 * @param entity Other entity
	 * @return True if the entities are equal
	 */
	public boolean equals(Entity entity) {
		return entity == this || (entity != null && id == entity.id);
	}
	
	/**
	 * Formats and prints the names of this entity's parents.
	 */
	public void printHierarchy() {
		if (transform.parent == null) {
			logger.log(getName());
		} else {
			Entity parent = transform.parent.getEntity();
			StringBuilder hierarchyString = new StringBuilder(getName());
			
			while (parent != null) {
				hierarchyString.insert(0, parent.getName() + " > ");
				parent = parent.transform.parent != null ? parent.transform.parent.getEntity() : null;
			}
			
			logger.log(hierarchyString);
		}
	}
	
	@Override
	public String toString() {
		String className = getClass().getSimpleName();
		int componentCount = components.size();
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = components.get(i).getClass();
			componentNames[i] = componentClass.getSimpleName();
		}
		
		return String.format("%s: %s (%s, %s) [%s] (%s) {%S}",
			className,
			getName(),
			transform.getGlobalX(),
			transform.getGlobalY(),
			String.join(", ", componentNames),
			componentCount,
			transform.getDepthIndex()
		);
	}
}
