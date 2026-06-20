package dev.prozilla.pine.core.entity.prefab;

import dev.prozilla.pine.core.component.RenderLayer;
import dev.prozilla.pine.core.entity.Entity;

public class LayerPrefab extends Prefab {
	
	protected boolean renderChildrenBelow;
	
	public LayerPrefab() {
		renderChildrenBelow = false;
	}
	
	public LayerPrefab setRenderChildrenBelow(boolean renderChildrenBelow) {
		this.renderChildrenBelow = renderChildrenBelow;
		return this;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new RenderLayer(renderChildrenBelow));
	}
}
