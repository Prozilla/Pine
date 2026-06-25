package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;

import java.util.Objects;

public class Meshes<A extends Mesh, B extends Mesh> extends Mesh {

	protected A meshA;
	protected B meshB;
	
	public Meshes(A meshA, B meshB) {
		this(meshA, meshB, new Vector3f());
	}
	
	public Meshes(A meshA, B meshB, Vector3f origin) {
		super(origin);
		this.meshA = meshA;
		this.meshB = meshB;
	}
	
	public A getMeshA() {
		return meshA;
	}
	
	public B getMeshB() {
		return meshB;
	}
	
	public void setMeshA(A meshA) {
		if (Objects.equals(this.meshA, meshA)) {
			return;
		}
		this.meshA = meshA;
		markAsDirty();
	}
	
	public void setMeshB(B meshB) {
		if (Objects.equals(this.meshB, meshB)) {
			return;
		}
		this.meshB = meshB;
		markAsDirty();
	}
	
	@Override
	protected float[] generateVertices() {
		float[] verticesA = meshA.getVertices();
		float[] verticesB = meshB.getVertices();
		
		if (verticesA == null) {
			return verticesB;
		} else if (verticesB == null) {
			return verticesA;
		}
		
		float[] vertices = new float[verticesA.length + verticesB.length];
		System.arraycopy(verticesA, 0, vertices, 0, verticesA.length);
		System.arraycopy(verticesB, 0, vertices, verticesA.length, verticesB.length);
		return vertices;
	}
	
	@Override
	protected float[] generateUVs() {
		float[] uvArrayA = meshA.getUVArray();
		float[] uvArrayB = meshB.getUVArray();
		
		if (uvArrayA == null) {
			return uvArrayB;
		} else if (uvArrayB == null) {
			return uvArrayA;
		}
		
		float[] uvArray = new float[uvArrayA.length + uvArrayB.length];
		System.arraycopy(uvArrayA, 0, uvArray, 0, uvArrayA.length);
		System.arraycopy(uvArrayB, 0, uvArray, uvArrayA.length, uvArrayB.length);
		return uvArray;
	}
	
	@Override
	protected int[] generateTriangles() {
		int[] trianglesA = meshA.getTriangles();
		int[] trianglesB = meshB.getTriangles();
		
		if (trianglesA == null) {
			return trianglesB;
		} else if (trianglesB == null) {
			return trianglesA;
		}
		
		int[] triangles = new int[trianglesA.length + trianglesB.length];
		System.arraycopy(trianglesA, 0, triangles, 0, trianglesA.length);
		
		int vertexOffset = meshA.getVertices().length / 3;
		for (int i = 0; i < trianglesB.length; i++) {
			triangles[trianglesA.length + i] = trianglesB[i] + vertexOffset;
		}
		
		return triangles;
	}
	
	@Override
	public boolean equals(Object object) {
		return object == this || (object instanceof Meshes<?, ?> meshes && equals(meshes));
	}
	
	@Override
	public boolean equals(Mesh mesh) {
		return mesh == this || (mesh instanceof Meshes<?, ?> meshes && equals(meshes));
	}
	
	public boolean equals(Meshes<?, ?> meshes) {
		return meshes != null && Objects.equals(meshes.origin, origin) && Objects.equals(meshes.meshA, meshA) && Objects.equals(meshes.meshB, meshB);
	}
	
	@Override
	public Mesh clone() {
		return new Meshes<>(meshA, meshB, origin);
	}
}
