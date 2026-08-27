package dev.prozilla.pine.examples.audio;

import dev.prozilla.pine.common.asset.audio.AudioSource;
import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.math.vector.Vector2f;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.selection.SingleSelectionProperty;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.component.mesh.RectRenderer;
import dev.prozilla.pine.core.entity.Entity;
import dev.prozilla.pine.core.entity.prefab.mesh.CanvasPrefab;
import dev.prozilla.pine.core.entity.prefab.mesh.LinePrefab;
import dev.prozilla.pine.core.rendering.mesh.Canvas;
import dev.prozilla.pine.core.rendering.mesh.Line;
import dev.prozilla.pine.core.rendering.mesh.Rect;
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
	private Line cursorTrail;
	public Canvas canvas;
	
	// Constants
	public static final int BAR_COUNT = 25;
	public static final float BAR_GAP = 4;
	public static final float VOLUME = 0.21f;
	public static final int CURSOR_TRAIL_LENGTH = 10;
	public static final float CURSOR_TRAIL_THICKNESS = 10;
	
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
		
		cameraData.orthographic = true;
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
			source.setVolume(VOLUME);
			source.setLoop(true);
			source.play();
		});
		
		addSystem(new BarResizer(this));
		
		barPrefab = new BarPrefab();
		bars = new ArrayList<>();
		for (int i = 0; i < BAR_COUNT; i++) {
			addBar();
		}
		updateBars();
		
//		addEntity(new LinePrefab(new Line(new Vector3f(-200, 0, 0), List.of(
//			new Vector2f(-100, 10),
//			new Vector2f(5, 35),
//			new Vector2f(150, 20)
//		), 10)));
//
//		addEntity(new LinePrefab(new Line(new Vector3f(200, 0, 0), List.of(
//			new Vector2f(-100, 0),
//			new Vector2f(0, -50),
//			new Vector2f(100, 0),
//			new Vector2f(200, -50)
//		), 10)));
//
//		addEntity(new LinePrefab(new Line(new Vector3f(0, -100, 0), List.of(
//			new Vector2f(-100, 0),
//			new Vector2f(0, 0),
//			new Vector2f(100, 0)
//		), 10)));
		
		float[] cursorTrailThickness = new float[CURSOR_TRAIL_LENGTH];
		for (int i = 0; i < CURSOR_TRAIL_LENGTH; i++) {
			cursorTrailThickness[i] = (CURSOR_TRAIL_LENGTH - i) * CURSOR_TRAIL_THICKNESS / CURSOR_TRAIL_LENGTH;
		}
		
		cursorTrail = new Line(new Vector3f(), new ArrayList<>(), cursorTrailThickness);
		addEntity(new LinePrefab(cursorTrail));
		
		getTimer().startInterval(() -> {
			Vector2i cursor = getInput().getCursor();
			cursorTrail.addPoint(new Vector2f(cursor.x - cameraData.getCenterX(), -cursor.y + cameraData.getCenterY()));
			if (cursorTrail.getPointCount() > CURSOR_TRAIL_LENGTH) {
				cursorTrail.removePoint(0);
			}
		}, 0.05f);
		
		canvas = new Canvas()
			.functionCurve((x) -> MathUtils.square(x) / 100, -100, 100, 20)
			.parametricCurve((t) -> new Vector2f((float)Math.cos(t), (float)Math.sin(t)).scale(100), 0f, MathUtils.PI * 2, 100)
			.stroke();
		
		addEntity(new CanvasPrefab(canvas));
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
		Entity bar = addEntity(barPrefab);
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
			Rect rect = rectRenderer.getMesh();
			
			float position = (float)i / barCount;
			rect.setOriginX((position) * (Main.WIDTH));
			
			rect.setWidth((float)Main.WIDTH / barCount - BAR_GAP);
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
