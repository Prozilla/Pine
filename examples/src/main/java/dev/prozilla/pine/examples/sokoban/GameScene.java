package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.core.component.sprite.GridGroup;
import dev.prozilla.pine.core.entity.prefab.sprite.GridPrefab;
import dev.prozilla.pine.core.entity.prefab.sprite.TilePrefab;
import dev.prozilla.pine.core.scene.Scene;
import dev.prozilla.pine.examples.sokoban.entity.*;
import dev.prozilla.pine.examples.sokoban.system.CrateUpdater;
import dev.prozilla.pine.examples.sokoban.system.PlayerInputHandler;
import dev.prozilla.pine.examples.sokoban.system.PlayerMover;

public class GameScene extends Scene {
	
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
	
	private static final int TILE_SIZE = 64;
	
	@Override
	protected void load() {
		super.load();
		
		// Create systems
		world.addSystem(new PlayerInputHandler());
		world.addSystem(new PlayerMover());
		
		// Create grid entities
		GridPrefab gridPrefab = new GridPrefab(TILE_SIZE);
		GridGroup backgroundGrid = world.addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup goalGrid = world.addEntity(gridPrefab).getComponent(GridGroup.class);
		GridGroup foregroundGrid = world.addEntity(gridPrefab).getComponent(GridGroup.class);
		
		world.addSystem(new CrateUpdater(goalGrid));
		
		// Create tile entities
		BlockPrefab blockPrefab = new BlockPrefab();
		GroundPrefab groundPrefab = new GroundPrefab();
		GoalPrefab goalPrefab = new GoalPrefab();
		PlayerPrefab playerPrefab = new PlayerPrefab();
		CratePrefab cratePrefab = new CratePrefab();
		
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
						case 'x' -> cratePrefab;
						default -> null;
					};
					
					if (tilePrefab != null) {
						foregroundGrid.addTile(tilePrefab, j, i);
					}
				}
				
				backgroundGrid.addTile(groundPrefab, j, i);
			}
		}
		
		// Move camera to center of map
		int width = MAP[0].length();
		int height = MAP.length;
		
		cameraData.zoomIn(-0.1f);
		cameraData.getTransform().setPosition((width * TILE_SIZE) / 2f, (height * TILE_SIZE) / 2f);
	}
}
