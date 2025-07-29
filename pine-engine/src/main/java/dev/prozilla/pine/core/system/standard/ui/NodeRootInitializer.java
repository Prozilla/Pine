package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.entity.EntityEventType;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class NodeRootInitializer extends InitSystem {
	
	public NodeRootInitializer() {
		super(NodeRoot.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Entity entity = chunk.getEntity();
		NodeRoot nodeRoot = chunk.getComponent(NodeRoot.class);
		
		entity.addListener(EntityEventType.DESCENDANT_ADD, (event) -> onChildAdd(nodeRoot, event.getTarget()));
		entity.addListener(EntityEventType.DESCENDANT_REMOVE, (event) -> onChildRemove(nodeRoot, event.getTarget()));
		
		initializeChildren(nodeRoot, entity);
	}
	
	private void initializeChildren(NodeRoot nodeRoot, Entity entity) {
		for (Transform child : entity.transform.children) {
			onChildAdd(nodeRoot, child.entity);
			initializeChildren(nodeRoot, child.entity);
		}
	}
	
	private void onChildAdd(NodeRoot nodeRoot, Entity child) {
		Node node = child.getComponent(Node.class);
		if (node == null) {
			return;
		}
		
		if (node.tabIndex >= 0) {
			nodeRoot.focusableNodes.add(node);
			nodeRoot.focusableNodes.sort((nodeA, nodeB) -> {
				if (nodeA.tabIndex == 0 && nodeB.tabIndex == 0) {
					return 0;
				} else if (nodeA.tabIndex == 0) {
					return -1;
				} else if (nodeB.tabIndex == 0) {
					return 1;
				} else {
					return nodeB.getTransform().getDepthIndex() - nodeA.getTransform().getDepthIndex();
				}
			});
			
			if (node.autoFocus) {
				nodeRoot.focusNode(node);
			} else if (nodeRoot.focusedNodeIndex >= 0 && nodeRoot.focusableNodes.indexOf(node) <= nodeRoot.focusedNodeIndex) {
				nodeRoot.focusNextNode();
			}
		}
	}
	
	private void onChildRemove(NodeRoot nodeRoot, Entity child) {
		Node node = child.getComponent(Node.class);
		if (node == null) {
			return;
		}
		if (nodeRoot.focusedNodeIndex >= 0 && nodeRoot.focusableNodes.remove(node)) {
			nodeRoot.focusPreviousNode();
		}
	}
}
