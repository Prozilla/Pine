package dev.prozilla.pine.examples.snake.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.core.component.sprite.TileRenderer;

public class PlayerTailData extends Component {
	
	public PlayerData playerData;
	public TileRenderer previousTile;
	public TileRenderer currentTile;
	public TileRenderer nextTile;
	public boolean isDirty;
	
	public PlayerTailData(PlayerData playerData, TileRenderer tile) {
		this.playerData = playerData;
		currentTile = tile;
		
		previousTile = null;
		nextTile = null;
		isDirty = true;
	}
	
}
