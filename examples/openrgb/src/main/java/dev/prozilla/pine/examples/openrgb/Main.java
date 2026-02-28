package dev.prozilla.pine.examples.openrgb;

import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setInitialScene(new MainScene());
		applicationBuilder.setTitle("OpenRGB Client");
		applicationBuilder.setCompanyName("Pine");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setTargetFps(120);
		applicationBuilder.getRenderConfig().snapPixels.set(true);
		
		applicationBuilder.build().run();
	}
	
}
