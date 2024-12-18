package dev.prozilla.pine.common.math.vector;

/**
 * Represents 4 int values.
 */
public class Vector4i {
	
	public int x; // Left
	public int y; // Up
	public int z; // Right
	public int w; // Down
	
	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4i(int x, int y) {
		this.x = x;
		this.z = x;
		this.y = y;
		this.w = y;
	}
	
	public Vector4i(int x) {
		this.x = x;
		this.z = x;
		this.y = x;
		this.w = x;
	}
	
	public Vector4i() {
		this.x = 0;
		this.z = 0;
		this.y = 0;
		this.w = 0;
	}
	
}
