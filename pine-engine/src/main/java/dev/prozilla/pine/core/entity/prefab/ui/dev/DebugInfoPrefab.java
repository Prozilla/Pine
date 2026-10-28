package dev.prozilla.pine.core.entity.prefab.ui.dev;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.state.Timer;
import dev.prozilla.pine.core.state.Tracker;

public class DebugInfoPrefab extends TextPrefab {
	
	public DebugInfoPrefab(Timer timer, Tracker tracker) {
		setName("DebugInfo");
		setTag("debug-info");
		
		setText(() -> tracker.getTitle() + "\n"
			+ tracker.getSceneName() + "\n"
			+ tracker.getWindowSize() + "\n\n"
			+ timer.getFPS() + " fps\n"
			+ tracker.getRenderedVertices() + "/" + tracker.getTotalVertices() + " vertices\n\n"
			+ tracker.getEntityCount() + " entities\n"
			+ tracker.getComponentCount() + " components\n"
			+ tracker.getSystemCount() + " systems"
		);
		setFont(AssetPools.fonts.load("fonts/JetBrainsMono.ttf"));
		setStyleSheet("style/debug-info.css");
	}
	
}
