package dev.prozilla.pine.examples.tetris.entity.block;

import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class Cube extends Cuboid {
	
	@Override
	protected float[] generateUVs() {
		return new float[] {
			1, 0,
			1, 1,
			0, 1,
			0, 0,
			1, 0,
			1, 1,
			0, 1,
			0, 0,
			1, 0,
			1, 1,
			0, 1,
			0, 0,
			1, 0,
			1, 1,
			0, 1,
			0, 0,
			1, 0,
			1, 1,
			0, 1,
			0, 0,
			1, 0,
			1, 1,
			0, 1,
			0, 0
		};
	}
}
