package dev.prozilla.pine.examples.tetris.entity;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.core.entity.prefab.mesh.CuboidPrefab;
import dev.prozilla.pine.core.rendering.mesh.Cuboid;

public class GroundPrefab extends CuboidPrefab {

	public GroundPrefab() {
		super(new Cuboid(new Vector3f(10, 1, 10), new Vector3f(5, 1.5f, 5)));
	}

}
