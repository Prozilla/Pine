package dev.prozilla.pine.examples.flappybird.system.obstacle;

import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.system.render.RenderSystemBase;
import dev.prozilla.pine.examples.flappybird.component.PipeData;
import dev.prozilla.pine.examples.flappybird.component.PipesData;
import dev.prozilla.pine.examples.flappybird.component.PlayerData;
import dev.prozilla.pine.examples.flappybird.scene.GameScene;

public class PipesDebugRenderer extends RenderSystemBase {
	
	private GameScene gameScene;
	
	private static final Color COLOR = new Color(0, 255, 0, 175);
	
	public PipesDebugRenderer() {
		super(PipesData.class);
	}
	
	@Override
	public void initSystem(World world) {
		super.initSystem(world);
		gameScene = (GameScene)world.scene;
	}
	
	@Override
	public void render(Renderer renderer) {
		Vector2f playerPosition = gameScene.getCameraData().applyTransform(gameScene.player.transform.getGlobalX(), gameScene.player.transform.getGlobalY());
		renderer.drawRect(playerPosition.x, playerPosition.y, gameScene.player.transform.getDepth(), PlayerData.WIDTH, PlayerData.HEIGHT, COLOR);
		
		forEach(chunk -> process(chunk, renderer));
	}
	
	protected void process(EntityChunk chunk, Renderer renderer) {
		PipesData pipesData = chunk.getComponent(PipesData.class);
		
		Vector2f bottomPipePosition = gameScene.getCameraData().applyTransform(pipesData.bottomPipe.transform.getGlobalX(), pipesData.bottomPipe.transform.getGlobalY());
		Vector2f topPipePosition = gameScene.getCameraData().applyTransform(pipesData.topPipe.transform.getGlobalX(), pipesData.topPipe.transform.getGlobalY());
		
		renderer.drawRect(bottomPipePosition.x, bottomPipePosition.y + PipeData.RIM_HEIGHT, pipesData.bottomPipe.transform.getDepth(), PipeData.WIDTH, PipeData.HEIGHT, COLOR);
		renderer.drawRect(topPipePosition.x, topPipePosition.y, pipesData.topPipe.transform.getDepth(), PipeData.WIDTH, PipeData.HEIGHT, COLOR);
	}
}
