package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.asset.image.Texture;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.ApplicationBuilder;

public class Main {
	
	public static void main(String[] args) {
		ApplicationBuilder applicationBuilder = new ApplicationBuilder();
		
		applicationBuilder.setTitle("Sokoban");
		applicationBuilder.setCompanyName("Pine");
		applicationBuilder.setWindowSize(900, 600);
		applicationBuilder.setInitialScene(new GameScene());
		applicationBuilder.setIcons("images/crates/crate_03.png");
		applicationBuilder.setTargetFps(120);
		applicationBuilder.setApplicationManagerFactory(GameManager::new);
		applicationBuilder.getRenderConfig().snapPixels.setValue(true);
		applicationBuilder.setEnableLocalStorage(true);
		
		if (!Application.isDevMode()) {
			applicationBuilder.setFullscreen(true);
		}
		
		AssetPools.textures.setDefaultTextureFilter(Texture.Filter.NEAREST);
		AssetPools.textures.setDefaultTextureWrap(Texture.Wrap.MIRROR_CLAMP_TO_EDGE);
		
		applicationBuilder.build().run();
	}
}
