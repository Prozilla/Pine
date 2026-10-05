package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;

import java.util.Arrays;
import java.util.Objects;

public class StaticMesh extends Mesh {
	
	public StaticMesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray) {
		this(vertices, triangles, normals, uvArray, new Vector3f());
	}
	
	public StaticMesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray, Vector3f origin) {
		super(vertices, triangles, normals, uvArray, origin);
		
		if (normals == null || normals.length == 0) {
			recalculateNormals();
		}
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
	protected float[] generateNormals() {
		float[] normals = getNormals();
		if (isDirty() || normals == null || normals.length == 0) {
			normals = super.generateNormals();
		}
		return normals;
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
			       && Arrays.equals(staticMesh.getNormals(), getNormals())
			       && Arrays.equals(staticMesh.getUVArray(), getUVArray());
	}
	
	@Override
	public Mesh clone() {
		return new StaticMesh(getVertices(), getTriangles(), getNormals(), getUVArray(), origin.clone());
	}
	
}
