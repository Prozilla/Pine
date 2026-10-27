package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.VertexArrayObject;
import dev.prozilla.pine.core.rendering.VertexBufferObject;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import dev.prozilla.pine.core.system.render.RenderPass;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

/**
 * An optimized static mesh that reduces the amount of uploads to the GPU.
 * <p>
 *     In most situations, this mesh will only be uploaded to the GPU once, before its first render. It will only be
 *     uploaded again if the previous data cannot be reused for the current render (e.g., when rendering with different
 *     shaders).
 * </p>
 */
public class StaticMesh extends Mesh {
	
	private VertexArrayObject vertexArrayObject;
	private VertexBufferObject[] vertexBufferObjects;
	
	private ShaderProgram program;
	private Color color;
	private TextureAsset texture;
	
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
	public void draw(Renderer renderer, TextureAsset texture, Color color) {
		if (renderer.getRenderPass() == RenderPass.SHADOW) {
			super.draw(renderer, texture, color);
			return;
		}
		
		ShaderProgram program = renderer.getProgram();
		boolean generated = generate();
		if (generated || !Objects.equals(program, this.program) || !Objects.equals(color, this.color) || !Objects.equals(texture, this.texture)) {
			upload(program, texture, color);
			this.program = program;
			this.color = Cloneable.cloneOf(color);
			this.texture = texture;
		}
		
		if (getVertices() == null || getTriangles() == null) {
			return;
		}
		
		if (vertexArrayObject != null && vertexBufferObjects != null) {
			if (texture != null) {
				texture.bind();
			}
			
			program.use();
			vertexArrayObject.bind();
			glDrawElements(GL_TRIANGLES, getTriangles().length, GL_UNSIGNED_INT, 0);
			renderer.increaseVertices(getTriangles().length);
			
			if (texture != null) {
				texture.unbind();
			}
		} else {
			renderer.drawTriangles(texture, getVertices(), getTriangles(), getNormals(), getUVArray(), color);
		}
	}
	
	/**
	 * Uploads the data of this mesh to the GPU.
	 */
	protected void upload(ShaderProgram program, TextureAsset texture, Color color) {
		float[] vertices = getVertices();
		int[] triangles = getTriangles();
		float[] normals = getNormals();
		float[] uvArray = getUVArray();
		if (vertices == null || triangles == null) {
			return;
		}
		
		if (vertexArrayObject == null) {
			vertexArrayObject = new VertexArrayObject();
		}
		if (vertexBufferObjects == null) {
			vertexBufferObjects = new VertexBufferObject[] { new VertexBufferObject(), new VertexBufferObject() };
		}
		
		int totalVertices = vertices.length / 3;
		boolean hasNormals = normals != null && normals.length == vertices.length;
		
		Vertex vertex = new Vertex();
		vertex.setColor(color);
		vertex.setTexture(texture);
		
		FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(totalVertices * program.getStrideLength());
		IntBuffer trianglesBuffer = MemoryUtil.memAllocInt(triangles.length);
		try {
			for (int i = 0; i < totalVertices; i++) {
				vertex.position.set(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
				if (hasNormals) {
					vertex.normal.set(normals[i * 3], normals[i * 3 + 1], normals[i * 3 + 2]);
				} else {
					vertex.normal.set(0);
				}
				vertex.textureCoordinates.x = uvArray[i * 2];
				vertex.textureCoordinates.y = uvArray[i * 2 + 1];
				program.writeVertex(verticesBuffer, vertex);
			}
			verticesBuffer.flip();
			trianglesBuffer.put(triangles).flip();
			
			vertexArrayObject.bind();
			
			vertexBufferObjects[0].bind(VertexBufferObject.Target.ARRAY_BUFFER);
			vertexBufferObjects[0].uploadData(VertexBufferObject.Target.ARRAY_BUFFER, verticesBuffer, VertexBufferObject.Usage.STATIC_DRAW);
			program.bind();
			
			vertexBufferObjects[1].bind(VertexBufferObject.Target.ELEMENT_ARRAY_BUFFER);
			vertexBufferObjects[1].uploadData(VertexBufferObject.Target.ELEMENT_ARRAY_BUFFER, trianglesBuffer, VertexBufferObject.Usage.STATIC_DRAW);
		} finally {
			MemoryUtil.memFree(verticesBuffer);
			MemoryUtil.memFree(trianglesBuffer);
		}
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
	public StaticMesh optimized() {
		return this;
	}
	
	@Override
	public Mesh clone() {
		return new StaticMesh(getVertices(), getTriangles(), getNormals(), getUVArray(), origin.clone());
	}
	
}
