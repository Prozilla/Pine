package dev.prozilla.pine.examples.tetris.system;

import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.system.update.UpdateSystem;
import dev.prozilla.pine.examples.tetris.GameScene;
import dev.prozilla.pine.examples.tetris.component.BlockData;
import dev.prozilla.pine.examples.tetris.component.Grid;

import java.util.ArrayList;
import java.util.List;

public class BlockUpdater extends UpdateSystem {
	
	private GameScene gameScene;
	
	public BlockUpdater() {
		super(BlockData.class);
	}
	
	@Override
	public void initSystem(Scene scene) {
		super.initSystem(scene);
		
		if (scene instanceof GameScene gameScene) {
			this.gameScene = gameScene;
		}
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getTransform();
		BlockData blockData = chunk.getComponent(BlockData.class);
		Grid grid = gameScene != null ? gameScene.getGrid() : null;
		
		if (!blockData.isFalling || grid == null) {
			return;
		}
		
		blockData.timeUntilNextMove -= deltaTime;
		if (blockData.timeUntilNextMove > 0) {
			return;
		}
		
		transform.translate(0, -1, 0);
		blockData.timeUntilNextMove = BlockData.TIME_BETWEEN_MOVES;
		
		if (grid.collides(transform.position, blockData.getRotatedPositions())) {
			transform.translate(0, 1, 0);
			blockData.isFalling = false;
			placeBlock(chunk, grid);
		}
	}
	
	private void placeBlock(EntityChunk chunk, Grid grid) {
		Entity blockEntity = chunk.getEntity();
		Transform transform = blockEntity.transform;
		BlockData blockData = chunk.getComponent(BlockData.class);
		
		Vector3i[] positions = blockData.getRotatedPositions();
		
		List<Transform> childrenSnapshot = new ArrayList<>(transform.children);
		for (int i = 0; i < childrenSnapshot.size() && i < positions.length; i++) {
			Transform childTransform = childrenSnapshot.get(i);
			Vector3i offset = positions[i];
			
			float worldX = transform.position.x + offset.x;
			float worldY = transform.position.y + offset.y;
			float worldZ = transform.position.z + offset.z;
			
			Entity child = childTransform.getEntity();
			blockEntity.removeChild(child);
			child.transform.setPosition(worldX, worldY, worldZ);
			
			grid.occupy(Grid.worldToGridX(worldX), Grid.worldToGridY(worldY), Grid.worldToGridZ(worldZ), child);
		}
		
		clearRows(grid);
		blockEntity.setActive(false);
		
		if (gameScene != null) {
			gameScene.spawnBlock();
		}
	}
	
	private void clearRows(Grid grid) {
		for (int row = 0; row < Grid.HEIGHT; row++) {
			if (grid.isRowFull(row)) {
				grid.clearRow(row);
				grid.shiftDownAbove(row);
				row--;
			}
		}
	}
	
}
