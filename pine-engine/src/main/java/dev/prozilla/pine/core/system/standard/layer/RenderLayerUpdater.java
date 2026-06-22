package dev.prozilla.pine.core.system.standard.layer;

import dev.prozilla.pine.core.component.RenderLayer;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

import java.util.ArrayList;
import java.util.List;

public class RenderLayerUpdater extends UpdateSystemBase {
	
	private int maxZIndex;
	private float depthMultiplier;

	public RenderLayerUpdater() {
		this(1f);
	}

	public RenderLayerUpdater(float depthMultiplier) {
		super(RenderLayer.class);
		this.depthMultiplier = depthMultiplier;
	}
	
	public float getDepthMultiplier() {
		return depthMultiplier;
	}
	
	public void setDepthMultiplier(float depthMultiplier) {
		if (this.depthMultiplier == depthMultiplier) {
			return;
		}
		this.depthMultiplier = depthMultiplier;
		updateDepths();
	}

	@Override
	public void update(float deltaTime) {
		updateZIndices();
	}
	
	@Override
	public boolean shouldRun() {
		return false;
	}

	public void updateZIndices() {
		if ((world.initialized && !application.getConfig().enableDepthRecalculation.get()) || !hasEntityChunks()) {
			return;
		}
		
		List<RenderLayer> rootLayers = new ArrayList<>();
		for (EntityChunk chunk : getChunks()) {
			RenderLayer layer = chunk.getComponent(RenderLayer.class);
			if (isRootLayer(layer)) {
				rootLayers.add(layer);
			}
		}

		int zIndex = 0;
		for (RenderLayer rootLayer : rootLayers) {
			zIndex = rootLayer.calculateZIndex(zIndex);
		}
		maxZIndex = zIndex;
		updateDepths();
	}
	
	private boolean isRootLayer(RenderLayer layer) {
		Transform parent = layer.getTransform().parent;
		while (parent != null) {
			if (parent.getComponent(RenderLayer.class) != null) {
				return false;
			}
			parent = parent.parent;
		}
		return true;
	}
	
	public void updateDepths() {
		if (!hasEntityChunks()) {
			return;
		}
		
		for (EntityChunk chunk : getChunks()) {
			RenderLayer layer = chunk.getComponent(RenderLayer.class);
			if (maxZIndex == 0) {
				layer.getTransform().position.z = 0;
			} else {
				layer.getTransform().position.z = ((float)layer.getzIndex() / maxZIndex) * depthMultiplier;
			}
		}
		
		if (world.systemManager != null) {
			world.systemManager.updateEntityDepth();
		}
	}
}
