package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;

/**
 * Generates a circular shape.
 */
public class Circle extends Mesh {
	
	protected float radius;
	protected int edges;
	
	public Circle(Vector3f origin, float radius) {
		this(origin, radius, 0);
		setAutoEdges();
	}
	
	public Circle(Vector3f origin, float radius, int edges) {
		super(origin);
		this.radius = radius;
		this.edges = edges;
	}
	
	@Override
	protected float[] generateVertices() {
		if (edges < 3) {
			return null;
		}
		
		float[] vertices = new float[(edges + 1) * 3];
		
		vertices[0] = origin.x;
		vertices[1] = origin.y;
		vertices[2] = origin.z;
		
		for (int i = 0; i < edges; i++) {
			double angle = 2.0 * Math.PI * i / edges;
			float dx = (float) (Math.cos(angle) * radius);
			float dy = (float) (Math.sin(angle) * radius);
			vertices[(i + 1) * 3] = origin.x + dx;
			vertices[(i + 1) * 3 + 1] = origin.y + dy;
			vertices[(i + 1) * 3 + 2] = origin.z;
		}
		
		return vertices;
	}
	
	@Override
	protected float[] generateUVs() {
		float[] uvArray = new float[(edges + 1) * 2];
		
		uvArray[0] = 0.5f;
		uvArray[1] = 0.5f;
		
		for (int i = 0; i < edges; i++) {
			double angle = 2.0 * Math.PI * i / edges;
			uvArray[(i + 1) * 2] = (float) (Math.cos(angle) * 0.5 + 0.5);
			uvArray[(i + 1) * 2 + 1] = (float) (Math.sin(angle) * 0.5 + 0.5);
		}
		
		return uvArray;
	}
	
	@Override
	protected int[] generateTriangles() {
		int[] triangles = new int[edges * 3];
		for (int i = 0; i < edges; i++) {
			triangles[i * 3] = 0;
			triangles[i * 3 + 1] = i + 1;
			triangles[i * 3 + 2] = (i + 1) % edges + 1;
		}
		return triangles;
	}
	
	/**
	 * Returns the radius of this circle.
	 * @return The radius of this circle.
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Sets the radius of this circle.
	 * @param radius The new radius of this circle
	 */
	public void setRadius(float radius) {
		if (radius == this.radius) {
			return;
		}

		this.radius = radius;
		markAsDirty();
	}
	
	/**
	 * Returns the amount of edges of this circle.
	 * @return The amount of edges.
	 */
	public int getEdges() {
		return edges;
	}
	
	/**
	 * Automatically sets the amount of edges based on the radius.
	 */
	public void setAutoEdges() {
		setEdges(Math.round(radius / 2f));
	}
	
	/**
	 * Sets the amount of edges of this circle.
	 * @param edges The new amount of edges
	 */
	public void setEdges(int edges) {
		if (edges == this.edges) {
			return;
		}
		
		this.edges = edges;
		markAsDirty();
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Circle circle && equals(circle));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Circle circle && equals(circle));
	}
	
	public boolean equals(Circle circle) {
		return circle != null && circle.origin.equals(origin) && circle.radius == radius && circle.edges == edges;
	}
	
	@Override
	public Mesh clone() {
		return new Circle(origin, radius, edges);
	}
	
}
