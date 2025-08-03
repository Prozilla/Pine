package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shape.RoundedRect;
import dev.prozilla.pine.core.rendering.shape.Shape;
import dev.prozilla.pine.core.system.render.RenderSystem;

public class CircleRenderer extends RenderSystem {
	
	public CircleRenderer() {
		super(BarData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		Shape shape = new RoundedRect(renderer.getViewportCenter(), new Vector2f(100, 100), 50);
		TextureBase texture = AssetPools.textures.load("ground_06.png");
		shape.draw(renderer, texture, chunk.getTransform().getDepth());
	}
	
}
