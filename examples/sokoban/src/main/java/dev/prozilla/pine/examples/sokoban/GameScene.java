package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.asset.pool.AssetPools;
import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.style.StyleSheet;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.Application;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.core.state.input.Input;
import dev.prozilla.pine.core.state.input.Key;
import dev.prozilla.pine.core.state.input.ModifierKey;
import dev.prozilla.pine.examples.sokoban.entity.*;
import dev.prozilla.pine.examples.sokoban.entity.ui.UIPrefab;
import dev.prozilla.pine.examples.sokoban.system.CrateUpdater;
import dev.prozilla.pine.examples.sokoban.system.PlayerInputHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerMover;

public class GameScene extends Scene {
	
	private Vector2i previousCursorPosition;
	private boolean cameraMovementEnabled = false;
	
	public static final float MOVEMENT_SPEED = 300f;
	public static final float ROTATION_SPEED = 6f;
	
	private static final String[] MAP = {
		"OOOOOOOOOOOO  ",
		"O..  O     OOO",
		"O..  O x  x  O",
		"O..  OxOOOO  O",
		"O..    s OO  O",
		"O..  O O  x OO",
		"OOOOOO OOx x O",
		"  O x  x x x O",
		"  O    O     O",
		"  OOOOOOOOOOOO"
	};
	
//	private static final String[] MAP = {
//		"OOOOOO  OOO ",
//		"O..  O OOsOO",
//		"O..  OOO   O",
//		"O..     xx O",
//		"O..  O O x O",
//		"O..OOO O x O",
//		"OOOO x Ox  O",
//		"   O  xO x O",
//		"   O x  x  O",
//		"   O  OO   O",
//		"   OOOOOOOOO"
//	};

//	private static final String[] MAP = {
//		"###########",
//		"#---------#",
//		"#-$-$@$-$-#",
//		"#--$-$-$--#",
//		"#-$-$-$-$-#",
//		"#--$-$-$--#",
//		"#####$##$##",
//		"-#.....#-#",
//		"-#....*#-#",
//		"-#...*---#",
//		"-#....-###",
//		"-########"
//	};
	
	private static final int TILE_SIZE = 64;
	
	@Override
	protected void load() {
		super.load();
		
		cameraData.orthographic = true;
		
		// Create systems
		addSystem(new PlayerInputHandler());
		addSystem(new PlayerMover());
		
		// Create grid entities
		GridPrefab gridPrefab = new GridPrefab(TILE_SIZE);
		GridGroup backgroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup goalGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup foregroundGrid = addEntity(gridPrefab).getComponent(GridGroup.class);
		
		addSystem(new CrateUpdater(goalGrid));
		
		// Create tile entities
		BlockPrefab blockPrefab = new BlockPrefab();
		GroundPrefab groundPrefab = new GroundPrefab();
		GoalPrefab goalPrefab = new GoalPrefab();
		PlayerPrefab playerPrefab = new PlayerPrefab();
		CratePrefab cratePrefab = new CratePrefab();
		
		GameManager.instance.totalCrates = 0;
		
		for (int i = 0; i < MAP.length; i++) {
			String row = MAP[i];
			
			for (int j = 0; j < row.length(); j++) {
				char tileName = row.charAt(j);
				
				if (tileName == '.') {
					goalGrid.addTile(goalPrefab, j, i);
				} else {
					TilePrefab tilePrefab = switch (tileName) {
						case 'O' -> blockPrefab;
						case 's' -> playerPrefab;
						case 'x' -> {
							GameManager.instance.totalCrates++;
							yield cratePrefab;
						}
						default -> null;
					};
					
					if (tilePrefab != null) {
						foregroundGrid.addTile(tilePrefab, j, i);
					}
				}
				
				backgroundGrid.addTile(groundPrefab, j, i);
			}
		}
		
		// Add user interface
		StyleSheet styleSheet = AssetPools.styleSheets.load("style/hud.css", Application.isDevMode());
		addEntity(new UIPrefab(styleSheet));
		
		cameraData.zoomIn(-0.1f);
		cameraData.setBackgroundColor(Color.hex("#596A6C"));
		cameraData.farClipPlane = 10000f;
		
		if (renderLayerUpdater != null) {
			renderLayerUpdater.setDepthMultiplier(25f);
		}
		
		resetCamera();
	}
	
	@Override
	public void input(float deltaTime) throws IllegalStateException {
		super.input(deltaTime);
		
		Input input = getInput();
		if (input.getKeyDown(Key.F) || (input.getKeyDown(Key.ESCAPE) && cameraMovementEnabled)) {
			cameraMovementEnabled = !cameraMovementEnabled;
			if (cameraMovementEnabled) {
				input.disableCursor();
				previousCursorPosition = null;
				cameraData.orthographic = false;
			} else {
				input.showCursor();
				cameraData.orthographic = true;
				resetCamera();
			}
		}
		
		if (cameraMovementEnabled) {
			Vector3f delta = new Vector3f();
			Transform cameraTransform = cameraData.getTransform();
			if (input.getKey(Key.UP_ARROW)) {
				delta.add(cameraTransform.getForward());
			}
			if (input.getKey(Key.DOWN_ARROW)) {
				delta.subtract(cameraTransform.getForward());
			}
			if (input.getKey(Key.RIGHT_ARROW)) {
				delta.add(cameraTransform.getRight());
			}
			if (input.getKey(Key.LEFT_ARROW)) {
				delta.subtract(cameraTransform.getRight());
			}
			if (input.getKey(Key.PAGE_UP)) {
				delta.add(cameraTransform.getUp());
			}
			if (input.getKey(Key.PAGE_DOWN)) {
				delta.subtract(cameraTransform.getUp());
			}
			if (!delta.isZero()) {
				delta.normalize();
				delta.scale(deltaTime * MOVEMENT_SPEED);
				if (input.getModifierKey(ModifierKey.SHIFT)) {
					delta.scale(3f);
				}
				cameraTransform.translate(delta);
			}
			
			Vector2i cursorPosition = input.getCursor(true);
			if (previousCursorPosition != null) {
				Vector2i cursorMovement = previousCursorPosition.subtract(cursorPosition);
				cameraTransform.rotate(-cursorMovement.y * deltaTime * ROTATION_SPEED, -cursorMovement.x * deltaTime * ROTATION_SPEED, 0);
				previousCursorPosition.set(cursorPosition.x, cursorPosition.y);
			} else {
				previousCursorPosition = cursorPosition.clone();
			}
		}
	}
	
	private void resetCamera() {
		// Move camera to center of map
		int width = MAP[0].length();
		int height = MAP.length;
		
		float depthMultiplier = 1f;
		if (renderLayerUpdater != null) {
			depthMultiplier = renderLayerUpdater.getDepthMultiplier();
		}
		
		cameraData.getTransform().reset();
		cameraData.getTransform().translate((width * TILE_SIZE) / 2f, (height * TILE_SIZE) / 2f, 10f * depthMultiplier);
	}
}
