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
		
		int vertexCount = edges + 2;
		float[] vertices = new float[vertexCount * 3];
		
		// Center point
		vertices[0] = origin.x;
		vertices[1] = origin.y;
		vertices[2] = origin.z;
		
		for (int i = 0; i <= edges; i++) {
			double angle = 2.0 * Math.PI * i / edges;
			float dx = (float) (Math.cos(angle) * radius);
			float dy = (float) (Math.sin(angle) * radius);
			vertices[(i + 1) * 3] = origin.x + dx;
			vertices[(i + 1) * 3 + 1] = origin.y + dy;
			vertices[(i + 1) * 3 + 2] = origin.z;
		}
		
		// Convert triangle fan to triangles
		float[] triangles = new float[edges * 3 * 3];
		for (int i = 0; i < edges; i++) {
			// center
			triangles[i * 6] = vertices[0];
			triangles[i * 6 + 1] = vertices[1];
			triangles[i * 6 + 2] = vertices[2];
			// edge i
			triangles[i * 6 + 3] = vertices[(i + 1) * 2];
			triangles[i * 6 + 4] = vertices[(i + 1) * 2 + 1];
			triangles[i * 6 + 5] = vertices[(i + 1) * 2 + 2];
			// edge i+1
			triangles[i * 6 + 6] = vertices[(i + 2) * 2];
			triangles[i * 6 + 7] = vertices[(i + 2) * 2 + 1];
			triangles[i * 6 + 8] = vertices[(i + 2) * 2 + 2];
		}
		
		return triangles;
	}
	
	@Override
	protected float[] generateUVs() {
		float[] uvArray = new float[edges * 3 * 2];
		
		float centerU = 0.5f;
		float centerV = 0.5f;
		
		for (int i = 0; i < edges; i++) {
			double angle1 = 2.0 * Math.PI * i / edges;
			double angle2 = 2.0 * Math.PI * (i + 1) / edges;
			
			// UVs for the edge points
			float u1 = (float) (Math.cos(angle1) * 0.5 + 0.5f);
			float v1 = (float) (Math.sin(angle1) * 0.5 + 0.5f);
			
			float u2 = (float) (Math.cos(angle2) * 0.5 + 0.5f);
			float v2 = (float) (Math.sin(angle2) * 0.5 + 0.5f);
			
			uvArray[i * 6] = centerU;
			uvArray[i * 6 + 1] = centerV;
			uvArray[i * 6 + 2] = u1;
			uvArray[i * 6 + 3] = v1;
			uvArray[i * 6 + 4] = u2;
			uvArray[i * 6 + 5] = v2;
		}
		
		return uvArray;
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
