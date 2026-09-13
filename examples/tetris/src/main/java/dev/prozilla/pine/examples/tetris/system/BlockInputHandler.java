package dev.prozilla.pine.examples.tetris.system;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.system.input.InputSystem;
import dev.prozilla.pine.examples.tetris.GameScene;
import dev.prozilla.pine.examples.tetris.component.BlockData;
import dev.prozilla.pine.examples.tetris.component.Grid;

public class BlockInputHandler extends InputSystem {
	
	private static final Vector3i ROTATE_Y = new Vector3i(0, 1, 0);
	private static final Vector3i ROTATE_X = new Vector3i(1, 0, 0);
	private static final Vector3i ROTATE_Z = new Vector3i(0, 0, 1);
	
	private GameScene gameScene;
	
	public BlockInputHandler() {
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
	protected void process(EntityChunk chunk, Input input, float deltaTime) {
		Transform transform = chunk.getTransform();
		BlockData blockData = chunk.getComponent(BlockData.class);
		Grid grid = gameScene != null ? gameScene.getGrid() : null;
		
		if (grid == null) {
			return;
		}
		
		if (input.getKeyDown(Key.SPACE)) {
			hardDrop(transform, blockData, grid);
			return;
		}
		
		if (input.getKeyDown(Key.R)) {
			rotateBlock(blockData, transform, grid, ROTATE_Y);
		}
		if (input.getKeyDown(Key.T)) {
			rotateBlock(blockData, transform, grid, ROTATE_X);
		}
		if (input.getKeyDown(Key.G)) {
			rotateBlock(blockData, transform, grid, ROTATE_Z);
		}
		
		Vector3f delta = new Vector3f();
		if (input.getKeyRepeated(Key.UP_ARROW)) {
			delta.z -= 1;
		}
		if (input.getKeyRepeated(Key.DOWN_ARROW)) {
			delta.z += 1;
		}
		if (input.getKeyRepeated(Key.LEFT_ARROW)) {
			delta.x -= 1;
		}
		if (input.getKeyRepeated(Key.RIGHT_ARROW)) {
			delta.x += 1;
		}
		
		if (delta.isZero()) {
			return;
		}
		
		transform.translate(delta);
		if (grid.collides(transform.position, blockData.getRotatedPositions())) {
			transform.translate(delta.negate());
		}
	}
	
	@Override
	protected boolean isChunkActive(EntityChunk chunk) {
		return super.isChunkActive(chunk) && chunk.getComponent(BlockData.class).isFalling;
	}
	
	private void rotateBlock(BlockData blockData, Transform transform, Grid grid, Vector3i rotationDelta) {
		blockData.rotationIndex.add(rotationDelta);
		blockData.markAsDirty();
		
		if (!grid.collides(transform.position, blockData.getRotatedPositions())) {
			Vector3i[] positions = blockData.getRotatedPositions();
			int childCount = transform.getChildCount();
			for (int i = 0; i < childCount && i < positions.length; i++) {
				transform.getChild(i).transform.setPosition(positions[i].x, positions[i].y, positions[i].z);
			}
		} else {
			blockData.rotationIndex.subtract(rotationDelta);
			blockData.markAsDirty();
		}
	}
	
	private void hardDrop(Transform transform, BlockData blockData, Grid grid) {
		while (!grid.collides(transform.position, blockData.getRotatedPositions())) {
			transform.translate(0, -1, 0);
		}
		transform.translate(0, 1, 0);
		blockData.timeUntilNextMove = 0;
	}
	
}
