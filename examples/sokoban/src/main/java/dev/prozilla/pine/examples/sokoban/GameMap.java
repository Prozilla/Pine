package dev.prozilla.pine.examples.sokoban;

import dev.prozilla.pine.common.math.vector.Vector2i;

public final class GameMap {
	
	public static final String[] MAP = {
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
	
	private GameMap() {}
	
	public static Vector2i getSpawnPoint() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getTile(x, y) == 's') {
					return new Vector2i(x, y);
				}
			}
		}
		return new Vector2i(1, 1);
	}
	
	public static boolean contains(int x, int y) {
		return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
	}
	
	public static boolean isWall(int x, int y) {
		return getTile(x, y) == 'O';
	}
	
	public static char getTile(int x, int y) {
		return MAP[y].charAt(x);
	}
	
	public static int getWidth() {
		return MAP[0].length();
	}
	
	public static int getHeight() {
		return MAP.length;
	}
	
}
