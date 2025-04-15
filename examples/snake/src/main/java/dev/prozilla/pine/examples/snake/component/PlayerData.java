package dev.prozilla.pine.examples.snake.component;

import dev.prozilla.pine.core.component.Component;
import dev.prozilla.pine.examples.snake.GameScene;

import java.util.ArrayList;
import java.util.List;

public class PlayerData extends Component {
	
	/**
	 * Current direction (0: Up; 1: Left; 2: Down; 3: Right)
	 */
	public int direction;
	public int previousMoveDirection;
	public float timeUntilNextMove;
	public int score;
	
	public List<PlayerTailData> tails;
	
	public final GameScene scene;
	
	public static final float TIME_BETWEEN_MOVES = 0.15f;
	
	public PlayerData(GameScene scene) {
		this.scene = scene;
		
		direction = 0;
		previousMoveDirection = 0;
		timeUntilNextMove = TIME_BETWEEN_MOVES;
		score = 0;
		tails = new ArrayList<>();
	}
}
