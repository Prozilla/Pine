package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;

public class BlockPrefab extends TilePrefab {
	
	public BlockPrefab(int type) {
		super(String.format("images/blocks/block_%02d.png", type != 3 ? type + 1 : type + 5));
		setTag(EntityTag.BLOCK);
	}
	
}
