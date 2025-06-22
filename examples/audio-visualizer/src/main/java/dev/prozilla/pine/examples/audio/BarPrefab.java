package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.sprite.SpriteRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.sprite.SpritePrefab;

public class BarPrefab extends SpritePrefab {
	
	protected int index;
	
	public BarPrefab() {
		super("images/ground_06.png");
		setIndex(0);
	}
	
	public void setIndex(int index) {
		this.index = index;
		float position = (float)index / Main.BAR_COUNT;
		setColor(Color.hsl(position, 0.9f, 0.65f));
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		SpriteRenderer spriteRenderer = entity.getComponent(SpriteRenderer.class);
		spriteRenderer.scale.x = (float)Main.WIDTH / Main.BAR_COUNT / spriteRenderer.getWidth();
		entity.transform.position.x = ((float)index / Main.BAR_COUNT - 0.5f) * (Main.WIDTH - spriteRenderer.getWidth() * 2f);
		entity.transform.position.y = (spriteRenderer.getHeight() - Main.HEIGHT) / 2f;
		
		entity.addComponent(new BarData(index));
	}
}
