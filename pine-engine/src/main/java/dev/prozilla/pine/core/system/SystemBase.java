package dev.prozilla.pine.core.system;

import dev.prozilla.pine.common.Lifecycle;
import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.ComponentCollector;

import java.util.ArrayList;
import java.util.Objects;

// TO DO: ignore de-activated components

/**
 * System responsible for running logic for a specific type of component.
 * @param <C> Type of component to collect in this system.
 */
public abstract class SystemBase<C extends Component> implements Lifecycle {
	
	private final ComponentCollector<C> collector;
	
	protected World world;
	
	public SystemBase(Class<C> componentClass) {
		collector = new ComponentCollector<C>(componentClass);
	}
	
	public void init(World world) {
		Objects.requireNonNull(world, "World must not be null.");
		
		this.world = world;
	}
	
	@Override
	public void destroy() {
		collector.destroy();
	}
	
	/**
	 * Registers a component in this system's collection
	 * @see ComponentCollector
	 */
	public void registerComponent(Component component) {
		collector.register(component);
	}
	
	/**
	 * Returns all components in this system's collection.
	 * @see ComponentCollector
	 */
	protected ArrayList<C> getComponents() {
		return collector.components;
	}
	
	/**
	 * Returns true if this system has collected any components.
	 * @see ComponentCollector
	 */
	public boolean hasComponents() {
		return !collector.components.isEmpty();
	}
}
