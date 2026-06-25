package dev.prozilla.pine.examples.tetris.entity.block;

import dev.prozilla.pine.common.math.vector.Vector3i;

public class StraightBlockPrefab extends BlockPrefab {
	
	private static final Vector3i[] POSITIONS = new Vector3i[] {
		new Vector3i(0, 0, 0),
		new Vector3i(0, 1, 0),
		new Vector3i(0, 2, 0),
		new Vector3i(0, 3, 0),
	};
	
	public StraightBlockPrefab(String texturePath) {
		super(POSITIONS, texturePath);
	}
	
}
