package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * Generates a cuboid shape.
 */
public class Cuboid extends Mesh {
	
	protected final Vector3f size;
	
	public Cuboid() {
		this(Vector3f.one());
	}
	
	public Cuboid(Vector3f size) {
		this(size, size.clone().divide(-2f));
	}
	
	public Cuboid(Vector3f size, Vector3f origin) {
		super(origin);
		this.size = Checks.isNotNull(size, "size");
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
			// Front face
			x1, y1, z1,
			x1, y2, z1,
			x2, y2, z1,
			x2, y1, z1,
			
			// Back face
			x1, y1, z2,
			x1, y2, z2,
			x2, y2, z2,
			x2, y1, z2,
			
			// Left face
			x1, y1, z2,
			x1, y2, z2,
			x1, y2, z1,
			x1, y1, z1,
			
			// Right face
			x2, y1, z1,
			x2, y2, z1,
			x2, y2, z2,
			x2, y1, z2,
			
			// Top face
			x1, y2, z1,
			x1, y2, z2,
			x2, y2, z2,
			x2, y2, z1,
			
			// Bottom face
			x1, y1, z2,
			x1, y1, z1,
			x2, y1, z1,
			x2, y1, z2
		};
	}
	
	@Override
	protected float[] generateUVs() {
		float faceWidth = 1f / 4f;
		float faceHeight = 1f / 3f;
		
		float u1Front = faceWidth;
		float u2Front = faceWidth * 2f;
		float v1Front = faceHeight;
		float v2Front = faceHeight * 2f;
		
		float u1Back = faceWidth * 3f;
		float u2Back = 1f;
		float v1Back = faceHeight;
		float v2Back = faceHeight * 2f;
		
		float u1Left = 0f;
		float u2Left = faceWidth;
		float v1Left = faceHeight;
		float v2Left = faceHeight * 2f;
		
		float u1Right = faceWidth * 2f;
		float u2Right = faceWidth * 3f;
		float v1Right = faceHeight;
		float v2Right = faceHeight * 2f;
		
		float u1Top = faceWidth;
		float u2Top = faceWidth * 2f;
		float v1Top = 0f;
		float v2Top = faceHeight;
		
		float u1Bottom = faceWidth;
		float u2Bottom = faceWidth * 2f;
		float v1Bottom = faceHeight * 2f;
		float v2Bottom = 1f;
		
		return new float[] {
			u2Front, v1Front,
			u2Front, v2Front,
			u1Front, v2Front,
			u1Front, v1Front,
			
			u1Back, v1Back,
			u1Back, v2Back,
			u2Back, v2Back,
			u2Back, v1Back,
			
			u2Left, v1Left,
			u2Left, v2Left,
			u1Left, v2Left,
			u1Left, v1Left,
			
			u2Right, v1Right,
			u2Right, v2Right,
			u1Right, v2Right,
			u1Right, v1Right,
			
			u2Top, v1Top,
			u2Top, v2Top,
			u1Top, v2Top,
			u1Top, v1Top,
			
			u2Bottom, v1Bottom,
			u2Bottom, v2Bottom,
			u1Bottom, v2Bottom,
			u1Bottom, v1Bottom
		};
	}
	
	@Override
	protected int[] generateTriangles() {
		return new int[] {
			0, 1, 2,
			0, 2, 3,
			4, 5, 6,
			4, 6, 7,
			8, 9, 10,
			8, 10, 11,
			12, 13, 14,
			12, 14, 15,
			16, 17, 18,
			16, 18, 19,
			20, 21, 22,
			20, 22, 23
		};
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
		markAsDirty();
	}
	
	public void setSizeY(float y) {
		if (y == size.y) {
			return;
		}
		
		size.y = y;
		markAsDirty();
	}
	
	public void setSizeZ(float z) {
		if (z == size.y) {
			return;
		}
		
		size.y = z;
		markAsDirty();
	}
	
	public void setSize(Vector3f size) {
		Checks.isNotNull(size, "size");
		
		if (size.equals(this.size)) {
			return;
		}
		
		this.size.set(size);
		markAsDirty();
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
