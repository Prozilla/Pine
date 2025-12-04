package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.event.EventDispatcher;

public class NodeEventDispatcher extends EventDispatcher<NodeEvent.Type, Node, NodeEvent> {
	
	@Override
	protected NodeEvent createEvent(NodeEvent.Type type, Node target) {
		return new NodeEvent(type, target);
	}
	
}
