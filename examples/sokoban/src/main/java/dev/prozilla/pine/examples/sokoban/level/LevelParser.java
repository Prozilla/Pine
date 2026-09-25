package dev.prozilla.pine.examples.sokoban.level;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.common.util.parser.SuppliedSequentialParser;

public class LevelParser extends SuppliedSequentialParser<Level> {
	
	public static final char WALL_CHAR = 'O';
	public static final char GOAL_CHAR = '.';
	public static final char CRATE_CHAR = 'x';
	public static final char SPAWN_CHAR = 's';
	
	public LevelParser() {
		super(Level::new);
	}
	
	@Override
	protected boolean parse() {
		int maxX = 0;
		int x = 0;
		int y = 0;
		
		while (!endOfInput()) {
			if (getChar() == '\n') {
				maxX = Math.max(maxX, x);
				x = 0;
				y++;
			} else {
				Vector2i coordinate = new Vector2i(x, y);
				
				switch (getChar()) {
					case WALL_CHAR -> intermediate.walls.add(coordinate);
					case GOAL_CHAR -> intermediate.goals.add(coordinate);
					case CRATE_CHAR -> intermediate.crates.add(coordinate);
					case SPAWN_CHAR -> intermediate.spawn = coordinate;
				}
				
				x++;
			}
			
			moveCursor();
		}
		
		intermediate.size = new Vector2i(maxX + 1, y + 1);
		
		return succeed();
	}
	
	@Override
	protected void setInput(String input) {
		super.setInput(input.trim());
	}
}
