package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.math.vector.Vector2f;

public class Circle extends Shape {
	
	private Vector2f position;
	private float radius;
	private int edges;
	
	public Circle(Vector2f position, float radius) {
		this(position, radius, 0);
		setAutoEdges();
	}
	
	public Circle(Vector2f position, float radius, int edges) {
		this.position = position;
		this.radius = radius;
		this.edges = edges;
	}
	
	@Override
	protected float[] generateVertices() {
		if (edges < 3) {
			return null;
		}
		
		int vertexCount = edges + 2;
		float[] vertices = new float[vertexCount * 2];
		
		// Center point
		vertices[0] = position.x;
		vertices[1] = position.y;
		
		for (int i = 0; i <= edges; i++) {
			double angle = 2.0 * Math.PI * i / edges;
			float dx = (float) (Math.cos(angle) * radius);
			float dy = (float) (Math.sin(angle) * radius);
			vertices[(i + 1) * 2] = position.x + dx;
			vertices[(i + 1) * 2 + 1] = position.y + dy;
		}
		
		// Convert triangle fan to triangles
		float[] triangles = new float[edges * 3 * 2];
		for (int i = 0; i < edges; i++) {
			// center
			triangles[i * 6] = vertices[0];
			triangles[i * 6 + 1] = vertices[1];
			// edge i
			triangles[i * 6 + 2] = vertices[(i + 1) * 2];
			triangles[i * 6 + 3] = vertices[(i + 1) * 2 + 1];
			// edge i+1
			triangles[i * 6 + 4] = vertices[(i + 2) * 2];
			triangles[i * 6 + 5] = vertices[(i + 2) * 2 + 1];
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
	
	public void setPosition(Vector2f position) {
		this.position = position;
		isDirty = true;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
		isDirty = true;
	}
	
	public void setAutoEdges() {
		setEdges(Math.round(radius / 2f));
	}
	
	public void setEdges(int edges) {
		this.edges = edges;
		isDirty = true;
	}
	
}
