package dev.prozilla.pine.examples.pacman;

import dev.prozilla.pine.core.Game;

public class Main {
	
	public static void main(String[] args) {
		try {
			Game game = new Game("PacMan", 800, 600);
			
			game.init();
			game.start();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
}
