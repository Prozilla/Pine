package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;

public class CratePrefab extends TilePrefab {
	
	public CratePrefab() {
		super("sokoban/Crates/crate_02.png");
		setTag(EntityTag.CRATE);
	}
	
}
