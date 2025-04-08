package dev.prozilla.pine.core.system.standard.ui.image;

import dev.prozilla.pine.core.component.ui.ImageNode;
import dev.prozilla.pine.core.component.ui.Node;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class ImageInitializer extends InitSystem {
	
	public ImageInitializer() {
		super(ImageNode.class, Node.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		ImageNode imageNode = chunk.getComponent(ImageNode.class);
		Node node = chunk.getComponent(Node.class);
		
		node.currentSize.x = imageNode.regionSize.x;
		node.currentSize.y = imageNode.regionSize.y;
	}
}
