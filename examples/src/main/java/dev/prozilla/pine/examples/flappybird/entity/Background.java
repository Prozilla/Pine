package dev.prozilla.pine.examples.flappybird.entity;

import dev.prozilla.pine.core.World;
import dev.prozilla.pine.core.entity.Sprite;
import dev.prozilla.pine.examples.flappybird.Main;
import dev.prozilla.pine.examples.flappybird.GameScene;
import dev.prozilla.pine.examples.flappybird.component.BackgroundData;

public class Background extends Sprite {
	
	public BackgroundData backgroundData;
	
	public Background(World world, int index) {
		super(world, "flappybird/background.png");
		
		backgroundData = new BackgroundData(index);
		addComponent(backgroundData);
	}
	
	@Override
	public String getName() {
		return getName("Background");
	}
}
