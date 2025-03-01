package dev.prozilla.pine.core.component;

import java.util.List;

public interface ComponentFinder {
	
	<ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass);
	
	<ComponentType extends Component> ComponentType getComponentInParent(Class<ComponentType> componentClass, boolean recursive);
	
	<ComponentType extends Component> List<ComponentType> getComponentsInChildren(Class<ComponentType> componentClass);
	
	<ComponentType extends Component> ComponentType getComponent(Class<ComponentType> componentClass);
	
	<ComponentType extends Component> List<ComponentType> getComponents(Class<ComponentType> componentClass);
	
}
