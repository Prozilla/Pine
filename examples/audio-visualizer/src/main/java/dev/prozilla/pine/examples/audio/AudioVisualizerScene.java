package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.shape.RectRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;

import java.util.ArrayList;
import java.util.List;

public class AudioVisualizerScene extends Scene {
	
	private AudioSource source;
	private BarPrefab barPrefab;
	private List<RectRenderer> bars;
	
	private int currentTrack;
	private static final String[] TRACKS = new String[] {
		"audio/KevinMacleod-PookatoriAndFriends.ogg",
		"audio/AndrewApplepie-KeepOnTrying.ogg",
		"audio/PixelPlayground.ogg",
	};
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.setBackgroundColor(Color.hsl(0f, 0f, 0.05f));
		
		currentTrack = -1;
		nextTrack();
		
		world.addSystem(new BarResizer());
		
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
		if (input.getKeyDown(Key.SPACE)) {
			source.togglePause();
		} else if (input.getKeyDown(Key.NUMPAD_ADD)) {
			addBar();
			updateBars();
		} else if (input.getKeyDown(Key.NUMPAD_SUBTRACT)) {
			removeBar();
			updateBars();
		} else if (input.getKeyDown(Key.N)) {
			nextTrack();
		}
	}
	
	private void nextTrack() {
		// Stop previous track
		if (source != null) {
			source.stop();
		}
		
		// Load next track
		currentTrack++;
		if (currentTrack >= TRACKS.length) {
			currentTrack = 0;
		}
		source = AssetPools.audioSources.load(TRACKS[currentTrack]);
		source.setCapture(true);
		source.init();
		
		// Start track
		source.setVolume(0.21f);
		source.setLoop(true);
		source.play();
	}
	
	private void addBar() {
		barPrefab.setIndex(bars.size());
		Entity bar = world.addEntity(barPrefab);
		bars.add(bar.getComponent(RectRenderer.class));
	}
	
	private void removeBar() {
		bars.removeLast().destroy();
	}
	
	private void updateBars() {
		for (int i = 0; i < bars.size(); i++) {
			RectRenderer rectRenderer = bars.get(i);
			Transform transform = rectRenderer.getTransform();
			
			float position = (float)i / bars.size();
			transform.position.x = (position - 0.5f) * (Main.WIDTH);
			
			rectRenderer.size.x = (float)Main.WIDTH / bars.size() - Main.BAR_GAP;
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
