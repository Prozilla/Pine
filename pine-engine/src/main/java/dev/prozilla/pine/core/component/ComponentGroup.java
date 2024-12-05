package dev.prozilla.pine.core.component;

import dev.prozilla.pine.core.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for components of an entity that match certain types.
 */
public class ComponentGroup {
	
	private final Component[] components;
	private final Class<? extends Component>[] componentClasses;
	private final Map<Class<? extends Component>, Integer> classToIndex;
	
	private boolean isEnabled;
	
	@SafeVarargs
	public ComponentGroup(Class<? extends Component>... componentClasses) {
		this.components = new Component[componentClasses.length];
		this.componentClasses = componentClasses;
		
		classToIndex = new HashMap<>();
		for (int i = 0; i < componentClasses.length; i++) {
			classToIndex.put(componentClasses[i], i);
		}
		
		isEnabled = false;
	}
	
	/**
	 * Sets all components in this group.
	 */
	public void setComponents(Component[] components) {
		for (int i = 0; i < components.length; i++) {
			setComponent(i, components[i]);
		}
	}
	
	/**
	 * Sets a component at the given index.
	 * @throws IllegalArgumentException If the component is not an instance of the right component class.
	 */
	public void setComponent(int index, Component component) throws IllegalArgumentException {
		if (!componentClasses[index].isInstance(component)) {
			throw new IllegalArgumentException("Component at index " + index + " must be of type " + componentClasses[index].getName());
		}
		components[index] = component;
	}
	
	/**
	 * Retrieves a component of the specified type.
	 * @throws IllegalArgumentException If no component is found.
	 */
	public <C extends Component> C getComponent(Class<C> type) throws IllegalArgumentException {
		if (!classToIndex.containsKey(type)) {
			throw new IllegalArgumentException("Invalid component type: " + type.getName());
		}
		
		C component = getComponent(classToIndex.get(type));
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
	
	public void checkStatus() {
		if (!getEntity().isActive()) {
			isEnabled = false;
			return;
		}
		
		boolean allComponentsEnabled = true;
		for (Component component : components) {
			if (!component.isEnabled) {
				allComponentsEnabled = false;
				break;
			}
		}
		isEnabled = allComponentsEnabled;
	}
	
	public Entity getEntity() throws IllegalStateException {
		if (components.length == 0) {
			throw new IllegalStateException("Component group is empty.");
		}
		
		return components[0].entity;
	}
	
	public boolean isEnabled() {
		checkStatus();
		return isEnabled;
	}
	
	public int size() {
		return components.length;
	}
	
	public void print() {
		String entityName = getEntity().getName();
		int componentCount = componentClasses.length;
		String[] componentNames = new String[componentCount];
		
		for (int i = 0; i < componentCount; i++) {
			Class<? extends Component> componentClass = componentClasses[i];
			componentNames[i] = componentClass.getSimpleName();
		}
		
		System.out.printf("ComponentGroup: %s: [%s] (%s)%n", entityName, String.join(", ", componentNames), componentCount);
	}
}
