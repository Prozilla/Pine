package dev.prozilla.pine.examples.flappybird;

import dev.prozilla.pine.core.Application;

public class Editor {
	
	public static void main(String[] args) {
		Application flappyBird = FlappyBird.getApplication().build();
		dev.prozilla.pine.Editor editor = new dev.prozilla.pine.Editor(flappyBird);
		editor.run();
	}
	
}
