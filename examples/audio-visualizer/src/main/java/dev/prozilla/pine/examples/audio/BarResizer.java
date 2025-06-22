package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.util.ArrayUtils;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.scene.World;
import dev.prozilla.pine.core.system.update.UpdateSystemBase;

public class BarResizer extends UpdateSystemBase {
	
	private AudioVisualizerScene scene;
	
	public BarResizer() {
		super(BarData.class, RectRenderer.class);
	}
	
	@Override
	public void initSystem(World world) {
		super.initSystem(world);
		scene = (AudioVisualizerScene)world.scene;
	}
	
	@Override
	public void update(float deltaTime) {
		AudioSource source = scene.getAudioSource();
		if (source == null) {
			return;
		}
		
		double[] magnitudes = source.getAverageMagnitudes(scene.getBarCount());
		if (magnitudes != null && Main.ENABLE_SHUFFLE) {
			ArrayUtils.shuffle(magnitudes, Main.SHUFFLE_SEED);
		}
		
		forEach((chunk) -> {
			BarData barData = chunk.getComponent(BarData.class);
			RectRenderer rectRenderer = chunk.getComponent(RectRenderer.class);
			
			float factor = 0.1f;
			if (magnitudes != null && source.isPlaying()) {
				double magnitude = magnitudes[barData.index];
				factor += (float)Math.log1p(magnitude * 10) * (0.25f + 0.75f * (float)Math.sin((float)barData.index / (scene.getBarCount() - 1) * Math.PI));
			}
			
			rectRenderer.size.y = MathUtils.lerp(rectRenderer.size.y, factor * 64f, deltaTime * Main.LERP_SPEED);
		});
	}
}
