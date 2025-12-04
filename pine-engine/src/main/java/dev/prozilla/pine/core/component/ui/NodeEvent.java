package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.event.Event;

public class NodeEvent extends Event<NodeEvent.Type, Node> {
	
	public enum Type {
		SELECTOR_CHANGE,
		/** Fired when the mouse is pressed on a node. */
		CLICK,
		/** Fired when a node has lost focus. */
		BLUR,
		/** Fired when a node has gained focus. */
		FOCUS,
		/** Fired when the value of an input node has changed. */
		INPUT
	}
	
	public NodeEvent(Type type, Node node) {
		super(type, node);
	}
	
}
