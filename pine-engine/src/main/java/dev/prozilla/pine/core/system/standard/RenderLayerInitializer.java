package dev.prozilla.pine.core.system.standard;

import dev.prozilla.pine.core.component.RenderLayer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.init.InitSystem;

public class RenderLayerInitializer extends InitSystem {
	
	public RenderLayerInitializer() {
		super(RenderLayer.class);
	}
	
	@Override
	protected void process(EntityChunk chunk) {
		RenderLayer renderLayer = chunk.getComponent(RenderLayer.class);
		
		chunk.getEntity().addListener(Entity.EventType.PARENT_UPDATE, (event) -> {
			renderLayer.updateParent();
		});
	}
}
