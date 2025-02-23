package dev.prozilla.pine.common.math.vector;

/**
 * Represents a 2-dimensional direction.
 */
public enum Direction {
	UP(0, 1) {
		@Override
		Direction opposite() {
			return Direction.DOWN;
		}
	},
	DOWN(0, -1) {
		@Override
		Direction opposite() {
			return Direction.UP;
		}
	},
	LEFT(-1, 0) {
		@Override
		Direction opposite() {
			return Direction.RIGHT;
		}
	},
	RIGHT(1, 0) {
		@Override
		Direction opposite() {
			return Direction.LEFT;
		}
	};
	
	public final int x;
	public final int y;
	
	Direction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return The opposite direction.
	 */
	abstract Direction opposite();
	
	/**
	 * @return This direction as a vector with float precision.
	 */
	public Vector2f toFloatVector() {
		return new Vector2f(x, y);
	}
	
	/**
	 * @return This direction as a vector with integer precision.
	 */
	public Vector2i toIntVector() {
		return new Vector2i(x, y);
	}
}
