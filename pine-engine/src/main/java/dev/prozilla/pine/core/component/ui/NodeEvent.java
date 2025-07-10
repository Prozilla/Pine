package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.event.Event;

public class NodeEvent extends Event<NodeEventType, Node> {
	
	public NodeEvent(NodeEventType nodeEventType, Node node) {
		super(nodeEventType, node);
	}
	
}
