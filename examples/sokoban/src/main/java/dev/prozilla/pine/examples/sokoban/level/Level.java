package dev.prozilla.pine.examples.sokoban.level;

import dev.prozilla.pine.common.math.vector.Vector2i;
import dev.prozilla.pine.extensions.pinet.packet.PacketBuffer;

import java.util.ArrayList;
import java.util.List;

public class Level {
	
	public Vector2i size;
	public List<Vector2i> walls;
	public List<Vector2i> goals;
	public List<Vector2i> crates;
	public Vector2i spawn;
	
	public static final String LEVEL_0 = """
		OOOOOOOOOOOO \s
		O..  O     OOO
		O..  O x  x  O
		O..  OxOOOO  O
		O..    s OO  O
		O..  O O  x OO
		OOOOOO OOx x O
		  O x  x x x O
		  O    O     O
		  OOOOOOOOOOOO
		""";
	public static final String LEVEL_1 = """
		OOOOOO  OOO
		O..  O OOsOO
		O..  OOO   O
		O..     xx O
		O..  O O x O
		O..OOO O x O
		OOOO x Ox  O
		   O  xO x O
		   O x  x  O
		   O  OO   O
		   OOOOOOOOO
		""";
	public static final String[] LEVELS = new String[]{
		LEVEL_0,
		LEVEL_1
	};
	
	public Level() {
		this(new Vector2i(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Vector2i());
	}
	
	public Level(Vector2i size, List<Vector2i> walls, List<Vector2i> goals, List<Vector2i> crates, Vector2i spawn) {
		this.size = size;
		this.walls = walls;
		this.goals = goals;
		this.crates = crates;
		this.spawn = spawn;
	}
	
	public boolean contains(Vector2i coordinate) {
		return coordinate.x >= 0 && coordinate.y >= 0 && coordinate.x < getWidth() && coordinate.y < getHeight();
	}
	
	public boolean isWall(Vector2i coordinate) {
		return walls.contains(coordinate);
	}
	
	public int getWidth() {
		return size.x;
	}
	
	public int getHeight() {
		return size.y;
	}
	
	public int getCrateCount() {
		return crates.size();
	}
	
	public static void encode(PacketBuffer buffer, Level level) {
		buffer.writeVector2i(level.size);
		buffer.writeList(level.walls);
		buffer.writeList(level.goals);
		buffer.writeList(level.crates);
		buffer.writeVector2i(level.spawn);
	}
	
	public static Level decode(PacketBuffer buffer) {
		Vector2i size = buffer.readVector2i();
		List<Vector2i> walls = buffer.readList(Vector2i.class);
		List<Vector2i> goals = buffer.readList(Vector2i.class);
		List<Vector2i> crates = buffer.readList(Vector2i.class);
		Vector2i spawn = buffer.readVector2i();
		return new Level(size, walls, goals, crates, spawn);
	}
	
}
