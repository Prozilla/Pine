package dev.prozilla.pine.core.entity.prefab.ui.dev;

import dev.prozilla.pine.core.entity.prefab.ui.TextPrefab;
import dev.prozilla.pine.core.state.Timer;

public class FPSCounterPrefab extends TextPrefab {
	
	public FPSCounterPrefab(Timer timer) {
		setName("FPSCounter");
		setTag("fps-counter");
		
		setText(() -> "fps: " + timer.getFPS());
		setStyleSheet("style/fps-counter.css");
	}
	
}
