package dev.prozilla.pine.examples.sokoban.entity;

import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.examples.sokoban.EntityTag;
import dev.prozilla.pine.examples.sokoban.component.CrateData;

public class CratePrefab extends TilePrefab {
	
	protected int type;
	
	public CratePrefab(int type) {
		super(CrateData.getNormalSprite(type));
		setTag(EntityTag.CRATE);
		this.type = type;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		entity.addComponent(new CrateData(type));
	}
	
}
