package dev.prozilla.pine.examples.canvas;

import dev.prozilla.pine.core.ApplicationBuilder;
import dev.prozilla.pine.examples.flappybird.GameScene;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder canvasDemo = new ApplicationBuilder();
		
		canvasDemo.setInitialScene(new CanvasScene());
		canvasDemo.setTitle("Canvas Demo");
		canvasDemo.setWindowSize(1200, 600);
		canvasDemo.setTargetFps(120);
		
		canvasDemo.buildAndRun();
	}
	
}
