package dev.prozilla.pine.examples.ui;

import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.scene.ViewScene;

import java.util.List;
import java.util.function.Supplier;

public class Main {
	
	private static final int sceneIndex = 1;
	private static final List<Supplier<Scene>> sceneFactories = List.of(
		MainScene::new,
		() -> new ViewScene("view.html", "view.css")
	);
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setInitialScene(sceneFactories.get(sceneIndex).get());
		applicationBuilder.setTitle("UI Demo");
		applicationBuilder.setCompanyName("Pine");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setTargetFps(120);
		applicationBuilder.getRenderConfig().snapPixels.set(true);
		applicationBuilder.getConfig().stopOnException.set(Application.isDevMode());
		
		applicationBuilder.build().run();
	}
	
}
