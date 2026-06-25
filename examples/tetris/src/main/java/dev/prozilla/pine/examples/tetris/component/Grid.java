package dev.prozilla.pine.examples.tetris.component;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.entity.Entity;

public class Grid {
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 20;
	public static final int DEPTH = 10;
	
	private final Entity[][][] cells;
	
	public Grid() {
		cells = new Entity[WIDTH][HEIGHT][DEPTH];
	}
	
	public void occupy(Vector3i position, Entity entity) {
		occupy(position.x, position.y, position.z, entity);
	}
	
	public void occupy(int x, int y, int z, Entity entity) {
		if (isValid(x, y, z)) {
			cells[x][y][z] = entity;
		}
	}
	
	public void free(Vector3i position) {
		free(position.x, position.y, position.z);
	}
	
	public void free(int x, int y, int z) {
		if (isValid(x, y, z)) {
			cells[x][y][z] = null;
		}
	}
	
	public boolean isOccupied(Vector3i position) {
		return isOccupied(position.x, position.y, position.z);
	}
	
	public boolean isOccupied(int x, int y, int z) {
		return isValid(x, y, z) && cells[x][y][z] != null;
	}
	
	public boolean isValid(Vector3i position) {
		return isValid(position.x, position.y, position.z);
	}
	
	public boolean isValid(int x, int y, int z) {
		return x >= 0 && x < WIDTH
			&& y >= 0 && y < HEIGHT
			&& z >= 0 && z < DEPTH;
	}
	
	public boolean isRowFull(int row) {
		for (int x = 0; x < WIDTH; x++) {
			for (int z = 0; z < DEPTH; z++) {
				if (!isOccupied(x, row, z)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void clearRow(int row) {
		for (int x = 0; x < WIDTH; x++) {
			for (int z = 0; z < DEPTH; z++) {
				Entity entity = cells[x][row][z];
				if (entity != null) {
					entity.destroy();
				}
				cells[x][row][z] = null;
			}
		}
	}
	
	public void shiftDownAbove(int row) {
		for (int y = row; y < HEIGHT - 1; y++) {
			for (int x = 0; x < WIDTH; x++) {
				for (int z = 0; z < DEPTH; z++) {
					Entity entity = cells[x][y + 1][z];
					cells[x][y][z] = entity;
					
					if (entity != null) {
						entity.transform.translate(0, -1, 0);
						free(x, y + 1, z);
					}
					
					cells[x][y + 1][z] = null;
				}
			}
		}
	}
	
	public boolean collides(Vector3f entityPosition, Vector3i[] offsets) {
		for (Vector3i offset : offsets) {
			int x = worldToGridX(entityPosition.x + offset.x);
			int y = worldToGridY(entityPosition.y + offset.y);
			int z = worldToGridZ(entityPosition.z + offset.z);
			
			if (!isValid(x, y, z) || isOccupied(x, y, z)) {
				return true;
			}
		}
		return false;
	}
	
	public static int worldToGridX(float worldX) {
		return Math.round(worldX + 4.5f);
	}
	
	public static int worldToGridY(float worldY) {
		return Math.round(worldY);
	}
	
	public static int worldToGridZ(float worldZ) {
		return Math.round(worldZ + 4.5f);
	}
	
	public static float gridToWorldX(int gridX) {
		return gridX - 4.5f;
	}
	
	public static float gridToWorldZ(int gridZ) {
		return gridZ - 4.5f;
	}
}
