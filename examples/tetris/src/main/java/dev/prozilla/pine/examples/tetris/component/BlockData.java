package dev.prozilla.pine.examples.tetris.component;

import dev.prozilla.pine.common.Memoizable;
import dev.prozilla.pine.common.math.vector.Vector3i;
import dev.prozilla.pine.core.component.Component;

public class BlockData extends Component implements Memoizable {
	
	public boolean isFalling = true;
	public float timeUntilNextMove = TIME_BETWEEN_MOVES;
	
	public Vector3i[] basePositions;
	public final Vector3i rotationIndex = new Vector3i();
	
	private Vector3i[] cachedPositions;
	private boolean dirty = true;
	
	public static final float TIME_BETWEEN_MOVES = 1;
	
	public BlockData(Vector3i[] basePositions) {
		this.basePositions = basePositions;
	}
	
	@Override
	public void markAsDirty() {
		dirty = true;
	}
	
	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	public Vector3i[] getRotatedPositions() {
		if (cachedPositions == null) {
			cachedPositions = new Vector3i[basePositions.length];
			for (int i = 0; i < basePositions.length; i++) {
				cachedPositions[i] = new Vector3i();
			}
		}
		if (dirty) {
			calculatePositions();
			dirty = false;
		}
		return cachedPositions;
	}
	
	private void calculatePositions() {
		int rotationX = rotationIndex.x % 4;
		int rotationY = rotationIndex.y % 4;
		int rotationZ = rotationIndex.z % 4;
		
		for (int i = 0; i < basePositions.length; i++) {
			Vector3i base = basePositions[i];
			Vector3i result = cachedPositions[i];
			
			int x = base.x;
			int y = base.y;
			int z = base.z;
			
			switch (rotationZ) {
				case 1 -> {
					x = -base.y;
					y = base.x;
				}
				case 2 -> {
					x = -base.x;
					y = -base.y;
				}
				case 3 -> {
					x = base.y;
					y = -base.x;
				}
			}
			
			switch (rotationX) {
				case 1 -> {
					int temp = y;
					y = -z;
					z = temp;
				}
				case 2 -> {
					y = -y;
					z = -z;
				}
				case 3 -> {
					int temp = y;
					y = z;
					z = -temp;
				}
			}
			
			switch (rotationY) {
				case 1 -> result.set(z, y, -x);
				case 2 -> result.set(-x, y, -z);
				case 3 -> result.set(-z, y, x);
				default -> result.set(x, y, z);
			}
		}
	}
	
}
