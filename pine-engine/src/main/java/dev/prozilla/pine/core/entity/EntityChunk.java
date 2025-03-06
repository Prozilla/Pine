package dev.prozilla.pine.core.entity;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.Transform;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for an entity and its components that match a system's query.
 * Provides quick access to the matching components of the entity.
 */
public class EntityChunk implements Printable {
	
	/** The corresponding entity. */
	private Entity entity;
	/** Components of the entity that match the query. */
	private final Component[] components;
	/** Types of the components that the query is searching for. */
	private final Class<? extends Component>[] componentTypes;
	/** Map of component types to their index in the array of components. */
	private final Map<Class<? extends Component>, Integer> typeToIndex;
	
	/** Indicates whether the entity and its matching components are active. */
	private boolean isActive;
	
	@SafeVarargs
	public EntityChunk(Class<? extends Component>... componentTypes) {
		this.components = new Component[componentTypes.length];
		this.componentTypes = componentTypes;
		
		typeToIndex = new HashMap<>();
		for (int i = 0; i < componentTypes.length; i++) {
			typeToIndex.put(componentTypes[i], i);
		}
		
		isActive = false;
	}
	
	/**
	 * Sets all components in this group.
	 * @throws IllegalArgumentException If the components are not attached to the same entity.
	 */
	public void setComponents(Component[] components) throws IllegalArgumentException {
		for (int i = 0; i < components.length; i++) {
			if (i == 0) {
				this.entity = components[i].entity;
			} else if (!components[i].entity.equals(this.entity)) {
				throw new IllegalArgumentException("Components must be attached to the same entity.");
			}
			
			setComponent(i, components[i]);
		}
	}
	
	/**
	 * Sets a component at the given index.
	 * @throws IllegalArgumentException If the component is not an instance of the right component class.
	 */
	public void setComponent(int index, Component component) throws IllegalArgumentException {
		if (!componentTypes[index].isInstance(component)) {
			throw new IllegalArgumentException("Component at index " + index + " must be of type " + componentTypes[index].getName());
		}
		components[index] = component;
	}
	
	/**
	 * Retrieves a component of the specified type.
	 * @throws IllegalArgumentException If no component is found.
	 */
	public <C extends Component> C getComponent(Class<C> type) throws IllegalArgumentException {
		if (!typeToIndex.containsKey(type)) {
			throw new IllegalArgumentException("Invalid component type: " + type.getName());
		}
		
		C component = getComponent(typeToIndex.get(type));
		if (component == null) {
			throw new IllegalArgumentException("Component of type " + type.getName() + " not found in group.");
		}
		
		return component;
	}
	
	/**
	 * Retrieves a component at a given index.
	 */
	@SuppressWarnings("unchecked")
	public <C extends Component> C getComponent(int index) {
		return (C)components[index];
	}
	
	public Entity getEntity() throws IllegalStateException {
		return entity;
	}
	
	public Transform getTransform() {
		return entity.transform;
	}
	
	public boolean isActive() {
		checkStatus();
		return isActive;
	}
	
	public void checkStatus() {
		if (!entity.isActive()) {
			isActive = false;
			return;
		}
		
		boolean allComponentsEnabled = true;
		for (Component component : components) {
			if (!component.isActive) {
				allComponentsEnabled = false;
				break;
			}
		}
		isActive = allComponentsEnabled;
	}
	
	/**
	 * Returns the amount of components in this match.
	 */
	public int size() {
		return components.length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntityChunk) {
			return equals((EntityChunk)obj);
		} else {
			return super.equals(obj);
		}
	}
	
	public boolean equals(EntityChunk entityChunk) {
		return entity.id == entityChunk.getEntity().id;
	}
	
	@Override
	public String toString() {
		String entityName = entity.getName();
		int componentCount = componentTypes.length;
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = componentTypes[i];
			componentNames[i] = componentClass.getSimpleName();
		}
		
		return String.format("EntityChunk: %s: [%s] (%s)", entityName, String.join(", ", componentNames), componentCount);
	}
}
