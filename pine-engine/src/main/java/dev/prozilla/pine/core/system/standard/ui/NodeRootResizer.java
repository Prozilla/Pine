package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

/**
 * Resizes canvases based on the window's dimensions.
 */
public class NodeRootResizer extends UpdateSystemBase {
	
	public NodeRootResizer() {
		super(NodeRoot.class);
	}
	
	@Override
	public void update(float deltaTime) {
		int width = application.getWindow().getWidth();
		int height = application.getWindow().getHeight();
		
		forEach(chunk -> {
			NodeRoot nodeRoot = chunk.getComponent(NodeRoot.class);
			
			nodeRoot.size.x = width;
			nodeRoot.size.y = height;
		});
	}
}
