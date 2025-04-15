package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;

public class BlockPrefab extends TilePrefab {
	
	public BlockPrefab() {
		super("sokoban/Blocks/block_01.png");
		setTag(EntityTag.BLOCK);
	}
	
}
