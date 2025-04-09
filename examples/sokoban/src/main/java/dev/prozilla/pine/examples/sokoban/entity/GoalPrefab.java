package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;

public class GoalPrefab extends TilePrefab {
	
	public GoalPrefab() {
		super("sokoban/Environment/environment_15.png");
		setTag(EntityTag.GOAL);
	}
	
}
