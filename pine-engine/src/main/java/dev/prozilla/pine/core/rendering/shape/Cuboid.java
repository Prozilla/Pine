package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * Generates a cuboid shape.
 */
public class Cuboid extends Shape {
	
	protected Vector3f position;
	protected Vector3f size;
	
	public Cuboid() {
		this(new Vector3f(), new Vector3f());
	}
	
	public Cuboid(Vector3f position, Vector3f size) {
		this.position = Checks.isNotNull(position, "position");
		this.size = Checks.isNotNull(size, "size");
	}
	
	@Override
	protected float[] generateVertices() {
		float x1 = this.position.x;
		float y1 = this.position.y;
		float z1 = this.position.z;
		float x2 = this.position.x + this.size.x;
		float y2 = this.position.y + this.size.y;
		float z2 = this.position.z + this.size.z;
		
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
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	public float getZ() {
		return position.z;
	}
	
	public void setX(float x) {
		if (x == position.x) {
			return;
		}
		
		position.x = x;
		isDirty = true;
	}
	
	public void setY(float y) {
		if (y == position.y) {
			return;
		}
		
		position.y = y;
		isDirty = true;
	}
	
	public void setZ(float z) {
		if (z == position.z) {
			return;
		}
		
		position.z = z;
		isDirty = true;
	}
	
	public void setPosition(Vector3f position) {
		Checks.isNotNull(position, "position");
		
		if (position.equals(this.position)) {
			return;
		}
		
		this.position = position;
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
	public boolean equals(Shape shape) {
		return shape == this || (shape instanceof Cuboid cuboid && equals(cuboid));
	}
	
	public boolean equals(Cuboid cuboid) {
		return cuboid != null && Objects.equals(cuboid.position, position) && Objects.equals(cuboid.size, size);
	}
	
	@Override
	public Cuboid clone() {
		return new Cuboid(position, size);
	}
	
}
