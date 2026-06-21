package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.ListUtils;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.mesh.modifier.MeshModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a mesh using vertex and UV arrays which respectively represent the vertex and texture coordinates of each triangle.
 */
public abstract class Mesh implements TexturedDrawable, Cloneable<Mesh> {
	
	// TODO: Replace with list of unique vertices + list of triangles (indices of vertices)
	private float[] vertices;
	private float[] uvArray;
	/** If {@code true}, the vertex and UV arrays of this mesh will be re-generated before the next draw call. */
	public boolean isDirty;
	
	private final List<MeshModifier> modifiers = new ArrayList<>();
	
	/**
	 * Creates a mesh with pre-generated geometry.
	 * @param vertices The vertex array
	 * @param uvArray The UV array
	 */
	public Mesh(float[] vertices, float[] uvArray) {
		this.vertices = vertices;
		this.uvArray = uvArray;
		isDirty = false;
	}
	
	/**
	 * Creates a mesh that will be generated before the first draw call.
	 */
	public Mesh() {
		isDirty = true;
	}
	
	/**
	 * Generates the arrays of vertices and texture coordinates for this mesh and applies each modifier.
	 */
	public void generate() {
		vertices = generateVertices();
		if (vertices != null && vertices.length > 0) {
			uvArray = generateUVs();
		}
		
		for (MeshModifier modifier : modifiers) {
			float[] newVertices = modifier.modifyVertices(vertices);
			uvArray = modifier.modifyUVs(vertices, newVertices, uvArray);
			vertices = newVertices;
		}
		
		isDirty = false;
	}
	
	/**
	 * Generates the vertex array for this mesh.
	 *
	 * <p>Odd elements define the x-component of the vertex and even elements define the y-component.</p>
	 * <p>Every three vertices (or six elements) define a triangle.</p>
	 * @return The array of vertices.
	 */
	abstract protected float[] generateVertices();
	
	/**
	 * Generates the UV array for this mesh.
	 *
	 * <p>Odd elements define the x-component of the texture coordinate and even elements define the y-component.</p>
	 * @return The array of texture coordinates.
	 */
	abstract protected float[] generateUVs();
	
	/**
	 * Draws this mesh using its vertex and UV arrays.
	 * @see Renderer#drawTriangles(TextureAsset, float[], float[], Color)
	 */
	@Override
	public void draw(Renderer renderer, TextureAsset texture, Color color) {
		if (isDirty) {
			generate();
		}
		
		if (vertices == null) {
			return;
		}
		
		renderer.drawTriangles(texture, vertices, uvArray, color);
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
		isDirty = true;
	}
	
	/**
	 * Removes a modifier from this mesh.
	 * @param modifier The modifier to remove
	 */
	public void removeModifier(MeshModifier modifier) {
		modifiers.remove(modifier);
		modifier.removeTarget(this);
		isDirty = true;
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
	
}
