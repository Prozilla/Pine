package dev.prozilla.pine.examples.tetris.entity.block;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.Prefab;
import dev.prozilla.pine.examples.tetris.component.BlockData;

public class BlockPrefab extends Prefab {
	
	private final Vector3i[] positions;
	
	public BlockPrefab(Vector3i[] positions, String texturePath) {
		this.positions = positions;
		for (Vector3i position : positions) {
			CubePrefab cubePrefab = new CubePrefab(texturePath);
			cubePrefab.setPosition(new Vector3f(position));
			addChild(cubePrefab);
		}
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		entity.addComponent(new BlockData(positions));
	}
}
