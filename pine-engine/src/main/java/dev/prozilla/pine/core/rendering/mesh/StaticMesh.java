package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;

import java.util.Arrays;
import java.util.Objects;

public class StaticMesh extends Mesh {
	
	public StaticMesh(float[] vertices, float[] uvArray, int[] triangles) {
		this(vertices, uvArray, triangles, new Vector3f());
	}
	
	public StaticMesh(float[] vertices, float[] uvArray, int[] triangles, Vector3f origin) {
		super(vertices, uvArray, triangles, origin);
	}
	
	@Override
	protected float[] generateVertices() {
		return getVertices();
	}
	
	@Override
	protected int[] generateTriangles() {
		return getTriangles();
	}
	
	@Override
	protected float[] generateUVs() {
		return getUVArray();
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof StaticMesh staticMesh && equals(staticMesh));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof StaticMesh staticMesh && equals(staticMesh));
	}
	
	public boolean equals(StaticMesh staticMesh) {
		return staticMesh != null && Objects.equals(staticMesh.origin, origin)
			       && Arrays.equals(staticMesh.getVertices(), getVertices())
			       && Arrays.equals(staticMesh.getTriangles(), getTriangles())
			       && Arrays.equals(staticMesh.getUVArray(), getUVArray());
	}
	
	@Override
	public Mesh clone() {
		return new StaticMesh(getVertices(), getUVArray(), getTriangles(), origin.clone());
	}
	
}
