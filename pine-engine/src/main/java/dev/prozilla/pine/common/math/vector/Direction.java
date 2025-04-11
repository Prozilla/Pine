package dev.prozilla.pine.common.math.vector;

import dev.prozilla.pine.common.util.ArrayUtils;

/**
 * Represents a 2-dimensional direction.
 */
public enum Direction {
	UP(0, 1, 90, "up") {
		@Override
		public Direction opposite() {
			return Direction.DOWN;
		}
		
		@Override
		public Direction rotateClockwise() {
			return Direction.RIGHT;
		}
		
		@Override
		public Direction rotateCounterclockwise() {
			return Direction.LEFT;
		}
	},
	DOWN(0, -1, 270, "down") {
		@Override
		public Direction opposite() {
			return Direction.UP;
		}
		
		@Override
		public Direction rotateClockwise() {
			return Direction.LEFT;
		}
		
		@Override
		public Direction rotateCounterclockwise() {
			return Direction.RIGHT;
		}
	},
	LEFT(-1, 0, 180, "left") {
		@Override
		public Direction opposite() {
			return Direction.RIGHT;
		}
		
		@Override
		public Direction rotateClockwise() {
			return Direction.UP;
		}
		
		@Override
		public Direction rotateCounterclockwise() {
			return Direction.DOWN;
		}
	},
	RIGHT(1, 0, 0, "right") {
		@Override
		public Direction opposite() {
			return Direction.LEFT;
		}
		
		@Override
		public Direction rotateClockwise() {
			return Direction.DOWN;
		}
		
		@Override
		public Direction rotateCounterclockwise() {
			return Direction.UP;
		}
	};
	
	public final int x;
	public final int y;
	public final float degrees;
	public final String string;
	
	public static final int COUNT = 4;
	
	Direction(int x, int y, float degrees, String string) {
		this.x = x;
		this.y = y;
		this.degrees = degrees;
		this.string = string;
	}
	
	/**
	 * @return The opposite direction.
	 */
	public abstract Direction opposite();
	
	/**
	 * @return The next direction in clockwise rotation.
	 */
	public abstract Direction rotateClockwise();
	
	/**
	 * @return The next direction in counterclockwise rotation.
	 */
	public abstract Direction rotateCounterclockwise();
	
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
	
	@Override
	public String toString() {
		return string;
	}
	
	public static Direction parse(String input) {
		return ArrayUtils.findByString(Direction.values(), input);
	}
	
}
