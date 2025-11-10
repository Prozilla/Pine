package dev.prozilla.pine.examples.flappybird.scene;

import dev.prozilla.pine.examples.flappybird.entity.MenuPrefab;

public class MenuScene extends SceneBase {
	
	@Override
	protected void load() {
		super.load();
		
		getInput().showCursor();
		
		world.addEntity(new MenuPrefab(font));
	}
}
