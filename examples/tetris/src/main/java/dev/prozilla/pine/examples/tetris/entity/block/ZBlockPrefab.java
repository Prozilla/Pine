package dev.prozilla.pine.examples.tetris.entity.block;

import dev.prozilla.pine.common.math.vector.Vector3i;

public class ZBlockPrefab extends BlockPrefab {
	
	private static final Vector3i[] POSITIONS = new Vector3i[] {
		new Vector3i(0, 0, 0),
		new Vector3i(1, 0, 0),
		new Vector3i(1, 1, 0),
		new Vector3i(2, 1, 0),
	};
	
	public ZBlockPrefab(String texturePath) {
		super(POSITIONS, texturePath);
	}
	
}
