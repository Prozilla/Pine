package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;

public class GroundPrefab extends TilePrefab {
	
	public GroundPrefab(int type) {
		super(String.format("images/ground/ground_%02d.png", type != 0 ? type + 4 : type + 1));
	}
	
}
