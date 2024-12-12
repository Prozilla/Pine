package dev.prozilla.pine.examples.snake.component;

import dev.prozilla.pine.core.component.Component;

public class PlayerData extends Component {
	
	/**
	 * Current direction (0: Up; 1: Left; 2: Down; 3: Right)
	 */
	public int direction;
	public int previousMoveDirection;
	public float timeUntilNextMove;
	
	public static final float TIME_BETWEEN_MOVES = 0.25f;
	
	public PlayerData() {
		direction = 0;
		previousMoveDirection = 0;
		
		timeUntilNextMove = TIME_BETWEEN_MOVES;
	}
}
