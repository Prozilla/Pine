package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * Generates a cuboid shape.
 */
public class Cuboid extends Mesh {
	
	protected Vector3f origin;
	protected Vector3f size;
	
	public Cuboid() {
		this(Vector3f.one());
	}
	
	public Cuboid(Vector3f size) {
		this(size, size.clone().divide(-2f));
	}
	
	public Cuboid(Vector3f size, Vector3f origin) {
		this.size = Checks.isNotNull(size, "size");
		this.origin = Checks.isNotNull(origin, "origin");
	}
	
	@Override
	protected float[] generateVertices() {
		float x1 = this.origin.x;
		float y1 = this.origin.y;
		float z1 = this.origin.z;
		float x2 = this.origin.x + this.size.x;
		float y2 = this.origin.y + this.size.y;
		float z2 = this.origin.z + this.size.z;
		
		return new float[] {
			// Front
			x1, y1, z1,
			x1, y2, z1,
			x2, y2, z1,
			x1, y1, z1,
			x2, y2, z1,
			x2, y1, z1,
			
			// Back
			x1, y1, z2,
			x1, y2, z2,
			x2, y2, z2,
			x1, y1, z2,
			x2, y2, z2,
			x2, y1, z2,
			
			// Left
			x1, y1, z2,
			x1, y2, z2,
			x1, y2, z1,
			x1, y1, z2,
			x1, y2, z1,
			x1, y1, z1,
			
			// Right
			x2, y1, z1,
			x2, y2, z1,
			x2, y2, z2,
			x2, y1, z1,
			x2, y2, z2,
			x2, y1, z2,
			
			// Top
			x1, y2, z1,
			x1, y2, z2,
			x2, y2, z2,
			x1, y2, z1,
			x2, y2, z2,
			x2, y2, z1,
			
			// Bottom
			x1, y1, z2,
			x1, y1, z1,
			x2, y1, z1,
			x1, y1, z2,
			x2, y1, z1,
			x2, y1, z2
		};
	}
	
	@Override
	protected float[] generateUVs() {
		float faceWidth = 1f / 4f;
		float faceHeight = 1f / 3f;
		
		// Front
		float u1Front = faceWidth;
		float u2Front = faceWidth * 2f;
		float v1Front = faceHeight;
		float v2Front = faceHeight * 2f;
		
		// Back
		float u1Back = faceWidth * 3f;
		float u2Back = 1f;
		float v1Back = faceHeight;
		float v2Back = faceHeight * 2f;
		
		// Left
		float u1Left = 0f;
		float u2Left = faceWidth;
		float v1Left = faceHeight;
		float v2Left = faceHeight * 2f;
		
		// Right
		float u1Right = faceWidth * 2f;
		float u2Right = faceWidth * 3f;
		float v1Right = faceHeight;
		float v2Right = faceHeight * 2f;
		
		// Top
		float u1Top = faceWidth;
		float u2Top = faceWidth * 2f;
		float v1Top = 0f;
		float v2Top = faceHeight;
		
		// Bottom
		float u1Bottom = faceWidth;
		float u2Bottom = faceWidth * 2f;
		float v1Bottom = faceHeight * 2f;
		float v2Bottom = 1f;
		
		return new float[] {
			// Front
			u2Front, v1Front,
			u2Front, v2Front,
			u1Front, v2Front,
			u2Front, v1Front,
			u1Front, v2Front,
			u1Front, v1Front,
			
			// Back
			u1Back, v1Back,
			u1Back, v2Back,
			u2Back, v2Back,
			u1Back, v1Back,
			u2Back, v2Back,
			u2Back, v1Back,
			
			// Left
			u2Left, v1Left,
			u2Left, v2Left,
			u1Left, v2Left,
			u2Left, v1Left,
			u1Left, v2Left,
			u1Left, v1Left,
			
			// Right
			u2Right, v1Right,
			u2Right, v2Right,
			u1Right, v2Right,
			u2Right, v1Right,
			u1Right, v2Right,
			u1Right, v1Right,
			
			// Top
			u2Top, v1Top,
			u2Top, v2Top,
			u1Top, v2Top,
			u2Top, v1Top,
			u1Top, v2Top,
			u1Top, v1Top,
			
			// Bottom
			u2Bottom, v1Bottom,
			u2Bottom, v2Bottom,
			u1Bottom, v2Bottom,
			u2Bottom, v1Bottom,
			u1Bottom, v2Bottom,
			u1Bottom, v1Bottom
		};
	}
	
	public float getX() {
		return origin.x;
	}
	
	public float getY() {
		return origin.y;
	}
	
	public float getZ() {
		return origin.z;
	}
	
	public void setX(float x) {
		if (x == origin.x) {
			return;
		}
		
		origin.x = x;
		isDirty = true;
	}
	
	public void setY(float y) {
		if (y == origin.y) {
			return;
		}
		
		origin.y = y;
		isDirty = true;
	}
	
	public void setZ(float z) {
		if (z == origin.z) {
			return;
		}
		
		origin.z = z;
		isDirty = true;
	}
	
	public void setOrigin(Vector3f origin) {
		Checks.isNotNull(origin, "position");
		
		if (origin.equals(this.origin)) {
			return;
		}
		
		this.origin = origin;
		isDirty = true;
	}
	
	public float getSizeX() {
		return size.x;
	}
	
	public float getSizeY() {
		return size.y;
	}
	
	public float getSizeZ() {
		return size.z;
	}
	
	public void setSizeX(float x) {
		if (x == size.x) {
			return;
		}
		
		size.x = x;
		isDirty = true;
	}
	
	public void setSizeY(float y) {
		if (y == size.y) {
			return;
		}
		
		size.y = y;
		isDirty = true;
	}
	
	public void setSizeZ(float z) {
		if (z == size.y) {
			return;
		}
		
		size.y = z;
		isDirty = true;
	}
	
	public void setSize(Vector3f size) {
		Checks.isNotNull(size, "size");
		
		if (size.equals(this.size)) {
			return;
		}
		
		this.size = size;
		isDirty = true;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Cuboid rect && equals(rect));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Cuboid cuboid && equals(cuboid));
	}
	
	public boolean equals(Cuboid cuboid) {
		return cuboid != null && Objects.equals(cuboid.origin, origin) && Objects.equals(cuboid.size, size);
	}
	
	@Override
	public Cuboid clone() {
		return new Cuboid(size, origin);
	}
	
}
