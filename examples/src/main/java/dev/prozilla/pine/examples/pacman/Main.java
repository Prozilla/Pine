package dev.prozilla.pine.examples.pacman;

import dev.prozilla.pine.core.Application;

public class Main {
	
	public static void main(String[] args) {
		try {
			Application application = new Application("PacMan", 800, 600);
			
			application.init();
			application.start();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		}
	}
	
}
