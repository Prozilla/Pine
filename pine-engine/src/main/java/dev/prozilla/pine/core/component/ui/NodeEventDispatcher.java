package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.event.EventDispatcher;

public class NodeEventDispatcher extends EventDispatcher<NodeEventType, Node, NodeEvent> {
	
	@Override
	protected NodeEvent createEvent(NodeEventType type, Node target) {
		return new NodeEvent(type, target);
	}
	
}
