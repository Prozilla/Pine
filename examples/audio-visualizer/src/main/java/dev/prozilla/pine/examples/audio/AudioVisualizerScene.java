package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.property.selection.SingleSelectionProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.rendering.shape.Rect;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

import java.util.ArrayList;
import java.util.List;

public class AudioVisualizerScene extends Scene {
	
	private AudioSource source;
	private BarPrefab barPrefab;
	private List<RectRenderer> bars;
	
	private final SingleSelectionProperty<String> tracks;
	
	public AudioVisualizerScene() {
		tracks = new SingleSelectionProperty<>(
			"audio/AndrewApplepie-KeepOnTrying.ogg",
			"audio/AndrewApplepie-PokemonInNYC.ogg",
			"audio/PixelPlayground.ogg",
			"audio/KevinMacleod-PookatoriAndFriends.ogg"
		);
	}
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.setBackgroundColor(Color.hsl(0f, 0f, 0.05f));
		
		tracks.addObserver((track) -> {
			// Stop previous track
			if (source != null) {
				source.stop();
			}
			
			// Load next track
			source = AssetPools.audioSources.load(track);
			source.setCapture(true);
			source.init();
			
			// Start track
			source.setVolume(0.21f);
			source.setLoop(true);
			source.play();
		});
		
		world.addSystem(new BarResizer(this));
		
		barPrefab = new BarPrefab();
		bars = new ArrayList<>();
		for (int i = 0; i < Main.BAR_COUNT; i++) {
			addBar();
		}
		updateBars();
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.NUMPAD_ADD)) {
			addBar();
			updateBars();
		} else if (input.getKeyDown(Key.NUMPAD_SUBTRACT)) {
			removeBar();
			updateBars();
		} else if (input.getKeyDown(Key.N)) {
			nextTrack();
		} else if (input.getKeyDown(Key.P)) {
			previousTrack();
		}
		
		if (source != null) {
			if (input.getKeyDown(Key.SPACE)) {
				source.togglePause();
			} else if (input.getKeyDown(Key.R)) {
				source.restart();
			}
		}
	}
	
	private void nextTrack() {
		tracks.selectNext();
	}
	
	private void previousTrack() {
		tracks.selectPrevious();
	}
	
	private void addBar() {
		barPrefab.setIndex(bars.size());
		Entity bar = world.addEntity(barPrefab);
		bars.add(bar.getComponent(RectRenderer.class));
	}
	
	private void removeBar() {
		if (!bars.isEmpty()) {
			bars.removeLast().getEntity().destroy();
		}
	}
	
	private void updateBars() {
		int barCount = bars.size();
		for (int i = 0; i < barCount; i++) {
			RectRenderer rectRenderer = bars.get(i);
			Rect rect = rectRenderer.getShape();
			
			float position = (float)i / barCount;
			rect.setX((position) * (Main.WIDTH));
			
			rect.setWidth((float)Main.WIDTH / barCount - Main.BAR_GAP);
			rectRenderer.color.setRGB(Color.hsl(position, 0.9f, 0.65f));
		}
	}
	
	public int getBarCount() {
		return bars.size();
	}
	
	public AudioSource getAudioSource() {
		return source;
	}
	
}
