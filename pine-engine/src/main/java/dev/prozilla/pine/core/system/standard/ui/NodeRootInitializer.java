package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public final class NodeRootInitializer extends InitSystem {
	
	public NodeRootInitializer() {
		super(NodeRoot.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		Entity entity = chunk.getEntity();
		NodeRoot nodeRoot = chunk.getComponent(NodeRoot.class);
		
		entity.addListener(Entity.EventType.DESCENDANT_ADD, (event) -> onChildAdd(nodeRoot, event.getTarget()));
		entity.addListener(Entity.EventType.DESCENDANT_REMOVE, (event) -> onChildRemove(nodeRoot, event.getTarget()));
		
		addChildren(nodeRoot, entity);
	}
	
	private void addChildren(NodeRoot nodeRoot, Entity entity) {
		for (Transform child : entity.transform.children) {
			onChildAdd(nodeRoot, child.entity);
			addChildren(nodeRoot, child.entity);
		}
	}
	
	private void onChildAdd(NodeRoot nodeRoot, Entity child) {
		onChildAdd(nodeRoot, child.getComponent(Node.class));
		
		// Add grandchildren
		for (Transform descendant : child.transform.children) {
			onChildAdd(nodeRoot, descendant.entity);
		}
	}
	
	private void onChildAdd(NodeRoot nodeRoot, Node node) {
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
		onChildRemove(nodeRoot, child.getComponent(Node.class));
		
		// Remove grandchildren
		for (Transform descendant : child.transform.children) {
			onChildRemove(nodeRoot, descendant.entity);
		}
	}
	
	private void onChildRemove(NodeRoot nodeRoot, Node node) {
		if (node == null) {
			return;
		}
		if (nodeRoot.focusedNodeIndex >= 0 && nodeRoot.focusableNodes.remove(node)) {
			nodeRoot.focusPreviousNode();
		}
	}
	
}
