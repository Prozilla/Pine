package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Memoizable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.common.util.collection.ListUtils;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.mesh.modifier.MeshModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a polygon mesh using a vertex array, UV array and an array of triangles, where each element corresponds to the index of a vertex in the vertex array.
 */
public abstract class Mesh implements MaterialRenderable, Cloneable<Mesh>, Memoizable {
	
	protected final Vector3f origin;
	private float[] vertices;
	private float[] normals;
	private int[] triangles;
	private float[] uvArray;
	/** If {@code true}, the vertices, triangles and UVs of this mesh will be re-generated before the next draw call. */
	private boolean isDirty;
	private boolean isGenerating;
	private boolean smoothShading;
	
	private final List<MeshModifier> modifiers = new ArrayList<>();
	
	/**
	 * Creates a mesh with pre-generated geometry.
	 * @param vertices The vertex array
	 * @param uvArray The UV array
	 * @param triangles The array of vertex indices representing triangles
	 */
	public Mesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray) {
		this(vertices, triangles, normals, uvArray, new Vector3f());
	}
	
	/**
	 * Creates a mesh at a given origin point with pre-generated geometry.
	 * @param vertices The vertex array
	 * @param uvArray The UV array
	 * @param triangles The array of vertex indices representing triangles
	 */
	public Mesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray, Vector3f origin) {
		this.vertices = vertices;
		this.triangles = triangles;
		this.normals = normals;
		this.uvArray = uvArray;
		this.origin = Checks.isNotNull(origin, "origin");
		isDirty = false;
		isGenerating = false;
		smoothShading = true;
	}
	
	/**
	 * Creates a mesh that will be generated before the first draw call.
	 */
	public Mesh() {
		this(new Vector3f());
	}
	
	/**
	 * Creates a mesh at a given origin point that will be generated before the first draw call.
	 */
	public Mesh(Vector3f origin) {
		this.origin = Checks.isNotNull(origin, "origin");
		markAsDirty();
	}
	
	/**
	 * Generates the vertices, triangles and UVs of this mesh and applies each modifier.
	 */
	public void generate() {
		if (!isDirty || isGenerating) {
			return;
		}
		
		isGenerating = true;
		vertices = generateVertices();
		if (vertices != null && vertices.length > 0) {
			triangles = generateTriangles();
			normals = generateNormals();
			uvArray = generateUVs();
		}
		
		if (!modifiers.isEmpty() && vertices != null) {
			for (MeshModifier modifier : modifiers) {
				MeshModifier.ModifiedMesh result = modifier.apply(vertices, triangles, normals, uvArray);
				vertices = result.vertices;
				triangles = result.triangles;
				normals = result.normals;
				uvArray = result.uvArray;
			}
		}
		
		isDirty = false;
		isGenerating = false;
	}
	
	/**
	 * Generates the vertex array for this mesh.
	 * <p>Every three elements define a vertex (x, y, z).</p>
	 * @return The array of vertices.
	 */
	abstract protected float[] generateVertices();
	
	/**
	 * Generates the triangles for this mesh.
	 * <p>Every three elements define a triangle, represented by the indices of the corresponding vertices.</p>
	 * @return The array of triangles.
	 */
	abstract protected int[] generateTriangles();
	
	public void recalculateNormals() {
		normals = generateNormals();
	}
	
	protected float[] generateNormals() {
		if (vertices == null || triangles == null) {
			return null;
		}
		
		int vertexCount = vertices.length / 3;
		int triangleCount = triangles.length / 3;
		float[] result = new float[smoothShading ? vertices.length : triangleCount * 3];
		
		for (int t = 0; t < triangleCount; t++) {
			int i0 = triangles[t * 3];
			int i1 = triangles[t * 3 + 1];
			int i2 = triangles[t * 3 + 2];
			
			if (i0 < 0 || i1 < 0 || i2 < 0 || i0 >= vertexCount || i1 >= vertexCount || i2 >= vertexCount) {
				continue;
			}
			
			int a = i0 * 3;
			int b = i1 * 3;
			int c = i2 * 3;
			
			float e1x = vertices[b] - vertices[a];
			float e1y = vertices[b + 1] - vertices[a + 1];
			float e1z = vertices[b + 2] - vertices[a + 2];
			
			float e2x = vertices[c] - vertices[a];
			float e2y = vertices[c + 1] - vertices[a + 1];
			float e2z = vertices[c + 2] - vertices[a + 2];
			
			float nx = e1y * e2z - e1z * e2y;
			float ny = e1z * e2x - e1x * e2z;
			float nz = e1x * e2y - e1y * e2x;
			
			if (smoothShading) {
				addNormal(result, a, nx, ny, nz);
				addNormal(result, b, nx, ny, nz);
				addNormal(result, c, nx, ny, nz);
			} else {
				addNormal(result, t * 3, nx, ny, nz);
			}
		}
		
		return result;
	}
	
	/**
	 * Generates the UV array for this mesh.
	 * <p>Every two elements define a UV coordinate (u, v).</p>
	 * @return The array of UV coordinates.
	 */
	abstract protected float[] generateUVs();
	
	/**
	 * Draws this mesh using its vertex and UV arrays.
	 * @see Renderer#drawTriangles(TextureAsset, float[], int[], float[], float[], Color)
	 */
	@Override
	public void draw(Renderer renderer, TextureAsset texture, Color color) {
		generate();
		
		if (vertices == null || triangles == null) {
			return;
		}
		
		renderer.drawTriangles(texture, vertices, triangles, normals, uvArray, color);
	}
	
	public float[] getVertices() {
		generate();
		return vertices;
	}
	
	public int[] getTriangles() {
		generate();
		return triangles;
	}
	
	public float[] getNormals() {
		generate();
		return normals;
	}
	
	public float[] getUVArray() {
		generate();
		return uvArray;
	}
	
	public Vector3f getOrigin() {
		return origin;
	}
	
	public float getOriginX() {
		return origin.x;
	}
	
	public float getOriginY() {
		return origin.y;
	}
	
	public float getOriginZ() {
		return origin.z;
	}
	
	public void setOriginX(float x) {
		if (x == origin.x) {
			return;
		}
		
		origin.x = x;
		markAsDirty();
	}
	
	public void setOriginY(float y) {
		if (y == origin.y) {
			return;
		}
		
		origin.y = y;
		markAsDirty();
	}
	
	public void setOriginZ(float z) {
		if (z == origin.z) {
			return;
		}
		
		origin.z = z;
		markAsDirty();
	}
	
	public void setOrigin(Vector3f origin) {
		Checks.isNotNull(origin, "position");
		
		if (origin.equals(this.origin)) {
			return;
		}
		
		this.origin.set(origin);
		markAsDirty();
	}
	
	public void setSmoothShading(boolean smoothShading) {
		if (this.smoothShading == smoothShading) {
			return;
		}
		
		this.smoothShading = smoothShading;
		markAsDirty();
	}
	
	/**
	 * Gets the first modifier of a given type.
	 * @param modifierType The type of modifier to search for
	 * @return The modifier of the given type, or {@code null} if there is none.
	 * @param <M> The type of modifier
	 */
	public <M extends MeshModifier> M getModifier(Class<M> modifierType) {
		return ListUtils.getInstance(modifiers, modifierType);
	}
	
	/**
	 * Adds a modifier to this mesh.
	 * @param modifier The modifier to add
	 */
	public void addModifier(MeshModifier modifier) {
		modifiers.add(modifier);
		modifier.addTarget(this);
		markAsDirty();
	}
	
	/**
	 * Removes a modifier from this mesh.
	 * @param modifier The modifier to remove
	 */
	public void removeModifier(MeshModifier modifier) {
		if (modifiers.remove(modifier)) {
			modifier.removeTarget(this);
			markAsDirty();
		}
	}
	
	@Override
	public void markAsDirty() {
		isDirty = true;
	}
	
	@Override
	public boolean isDirty() {
		return isDirty;
	}
	
	public <M extends Mesh> Meshes<Mesh, M> join(M mesh) {
		return new Meshes<>(this, mesh);
	}
	
	public Mesh cloneWithModifiers() {
		Mesh clone = clone();
		for (MeshModifier modifier : modifiers) {
			clone.addModifier(modifier);
		}
		return clone;
	}
	
	@Override
	public abstract Mesh clone();
	
	private static void addNormal(float[] normals, int index, float x, float y, float z) {
		normals[index] += x;
		normals[index + 1] += y;
		normals[index + 2] += z;
	}
	
}
