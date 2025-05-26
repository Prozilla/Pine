package dev.prozilla.pine.core.system.standard.ui;

import dev.prozilla.pine.core.component.ui.NodeRoot;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystemBase;

/**
 * Prepares the rendering of nodes.
 */
public class NodeRootRenderer extends RenderSystemBase {
	
	public NodeRootRenderer() {
		super(NodeRoot.class);
	}
	
	@Override
	public void render(Renderer renderer) {
		renderer.resetScale();
	}
}
