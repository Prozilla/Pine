package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.common.util.ListUtils;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.shape.modifier.ShapeModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shape using vertex and UV arrays which respectively represent the vertex and texture coordinates of each triangle.
 */
public abstract class Shape implements Renderable, Cloneable<Shape> {
	
	private float[] vertices;
	private float[] uvArray;
	/** If {@code true}, the vertex and UV arrays of this shape will be re-generated before the next draw call. */
	public boolean isDirty;
	
	private final List<ShapeModifier> modifiers = new ArrayList<>();
	
	/**
	 * Creates a shape with pre-generated geometry.
	 * @param vertices The vertex array
	 * @param uvArray The UV array
	 */
	public Shape(float[] vertices, float[] uvArray) {
		this.vertices = vertices;
		this.uvArray = uvArray;
		isDirty = false;
	}
	
	/**
	 * Creates a shape that will be generated before the first draw call.
	 */
	public Shape() {
		isDirty = true;
	}
	
	/**
	 * Generates the arrays of vertices and texture coordinates for this shape and applies each modifier.
	 */
	public void generate() {
		vertices = generateVertices();
		if (vertices != null && vertices.length > 0) {
			uvArray = generateUVs();
		}
		
		for (ShapeModifier modifier : modifiers) {
			float[] newVertices = modifier.modifyVertices(vertices);
			uvArray = modifier.modifyUVs(vertices, newVertices, uvArray);
			vertices = newVertices;
		}
		
		isDirty = false;
	}
	
	/**
	 * Generates the vertex array for this shape.
	 *
	 * <p>Odd elements define the x-component of the vertex and even elements define the y-component.</p>
	 * <p>Every three vertices (or six elements) define a triangle.</p>
	 * @return The array of vertices.
	 */
	abstract protected float[] generateVertices();
	
	/**
	 * Generates the UV array for this shape.
	 *
	 * <p>Odd elements define the x-component of the texture coordinate and even elements define the y-component.</p>
	 * @return The array of texture coordinates.
	 */
	abstract protected float[] generateUVs();
	
	/**
	 * Draws this shape using its vertex and UV arrays.
	 * @see Renderer#drawTriangles(TextureBase, float[], float, float[], Color)
	 */
	@Override
	public void draw(Renderer renderer, TextureBase texture, Color color, float depth) {
		if (isDirty) {
			generate();
		}
		
		if (vertices == null) {
			return;
		}
		
		renderer.drawTriangles(texture, vertices, depth, uvArray, color);
	}
	
	/**
	 * Gets the first modifier of a given type.
	 * @param modifierType The type of modifier to search for
	 * @return The modifier of the given type, or {@code null} if there is none.
	 * @param <M> The type of modifier
	 */
	public <M extends ShapeModifier> M getModifier(Class<M> modifierType) {
		return ListUtils.getInstance(modifiers, modifierType);
	}
	
	/**
	 * Adds a modifier to this shape.
	 * @param modifier The modifier to add
	 */
	public void addModifier(ShapeModifier modifier) {
		modifiers.add(modifier);
		modifier.addTarget(this);
		isDirty = true;
	}
	
	/**
	 * Removes a modifier from this shape.
	 * @param modifier The modifier to remove
	 */
	public void removeModifier(ShapeModifier modifier) {
		modifiers.remove(modifier);
		modifier.removeTarget(this);
		isDirty = true;
	}
	
	public Shape cloneWithModifiers() {
		Shape clone = clone();
		for (ShapeModifier modifier : modifiers) {
			clone.addModifier(modifier);
		}
		return clone;
	}
	
	@Override
	public abstract Shape clone();
	
}
