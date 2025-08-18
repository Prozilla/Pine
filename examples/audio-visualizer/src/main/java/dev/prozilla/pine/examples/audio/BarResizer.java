package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

public class BarResizer extends UpdateSystemBase {
	
	private final AudioVisualizerScene scene;
	
	// Constants
	public static final boolean ENABLE_SHUFFLE = true;
	public static final long SHUFFLE_SEED = 0;
	public static final float LERP_SPEED = 10;
	
	public BarResizer(AudioVisualizerScene scene) {
		super(BarData.class, RectRenderer.class);
		this.scene = scene;
	}
	
	@Override
	public void update(float deltaTime) {
		AudioSource source = scene.getAudioSource();
		if (source == null) {
			return;
		}
		
		double[] magnitudes = source.getAverageMagnitudes(scene.getBarCount());
		if (magnitudes != null && ENABLE_SHUFFLE) {
			ArrayUtils.shuffle(magnitudes, SHUFFLE_SEED);
		}
		
		forEach((chunk) -> {
			BarData barData = chunk.getComponent(BarData.class);
			RectRenderer rectRenderer = chunk.getComponent(RectRenderer.class);
			
			float factor = 0.1f;
			if (magnitudes != null && source.isPlaying()) {
				double magnitude = magnitudes[barData.index];
				factor += (float)Math.log1p(magnitude * 10) * (0.25f + 0.75f * (float)Math.sin((float)barData.index / (scene.getBarCount() - 1) * Math.PI));
			}
			
			rectRenderer.size.y = MathUtils.lerp(rectRenderer.size.y, factor * 64f, deltaTime * LERP_SPEED);
		});
	}
}
