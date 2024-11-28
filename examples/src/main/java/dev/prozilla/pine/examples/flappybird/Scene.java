package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.examples.flappybird.object.Background;
import dev.prozilla.pine.examples.flappybird.object.Player;

public class Scene extends dev.prozilla.pine.core.state.Scene {
	
	@Override
	protected void load() {
		super.load();
		
		Background[] backgrounds = new Background[Math.round((float)Main.WIDTH / Background.WIDTH + 0.5f) + 1];
		
		// Fill screen with background sprites
		for (int i = 0; i < backgrounds.length; i++) {
			Background background = new Background(game, i);
			add(background);
			backgrounds[i] = background;
		}
		
		add(new Player(game));
	}
}
