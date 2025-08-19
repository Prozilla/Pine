package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.system.render.RenderSystem;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PipesDebugRenderer extends RenderSystem {
	
	public PipesDebugRenderer() {
		super(PipesData.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, Renderer renderer) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		GameScene gameScene = pipesData.gameScene;
		
		Vector2f playerPosition = gameScene.getCameraData().applyTransform(gameScene.player.transform.getGlobalX(), gameScene.player.transform.getGlobalY());
		Vector2f bottomPipePosition = gameScene.getCameraData().applyTransform(pipesData.bottomPipe.transform.getGlobalX(), pipesData.bottomPipe.transform.getGlobalY());
		Vector2f topPipePosition = gameScene.getCameraData().applyTransform(pipesData.topPipe.transform.getGlobalX(), pipesData.topPipe.transform.getGlobalY());
		
		renderer.drawRect(playerPosition.x, playerPosition.y, gameScene.player.transform.getDepth(), PlayerData.WIDTH, PlayerData.HEIGHT);
		renderer.drawRect(bottomPipePosition.x, bottomPipePosition.y, pipesData.bottomPipe.transform.getDepth(), PipeData.WIDTH, PipeData.HEIGHT);
		renderer.drawRect(topPipePosition.x, topPipePosition.y, pipesData.topPipe.transform.getDepth(), PipeData.WIDTH, PipeData.HEIGHT);
	}
}
