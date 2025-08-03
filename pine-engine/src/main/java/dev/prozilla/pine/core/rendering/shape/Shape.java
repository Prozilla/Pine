package dev.prozilla.pine.core.rendering.shape;

import dev.prozilla.pine.common.asset.image.TextureBase;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;

import java.util.ArrayList;
import java.util.List;


public abstract class Shape implements Renderable {
	
	private float[] vertices;
	private float[] uvArray;
	protected boolean isDirty;
	
	private final List<ShapeModifier> modifiers = new ArrayList<>();
	
	public Shape(float[] vertices, float[] uvArray) {
		this.vertices = vertices;
		this.uvArray = uvArray;
		isDirty = false;
	}
	
	public Shape() {
		isDirty = true;
	}
	
	/**
	 * Generates the arrays of vertices and texture coordinates for this shape.
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
	
	public void addModifier(ShapeModifier modifier) {
		modifiers.add(modifier);
	}
	
	public void removeModifier(ShapeModifier modifier) {
		modifiers.remove(modifier);
	}
	
}
