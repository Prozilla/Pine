package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.asset.image.TextureAsset;
import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.system.Color;
import dev.prozilla.pine.core.rendering.Renderer;
import dev.prozilla.pine.core.rendering.Vertex;
import dev.prozilla.pine.core.rendering.VertexArrayObject;
import dev.prozilla.pine.core.rendering.VertexBufferObject;
import dev.prozilla.pine.core.rendering.shader.ShaderProgram;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
	
	private final Map<ShaderProgram, Upload> uploads;
	private VertexBufferObject trianglesBuffer;
	private int trianglesVersion;
	
	private static final Vertex vertex = new Vertex();
	
	public StaticMesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray) {
		this(vertices, triangles, normals, uvArray, new Vector3f());
	}
	
	public StaticMesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray, Vector3f origin) {
		super(vertices, triangles, normals, uvArray, origin);
		
		uploads = new HashMap<>();
		trianglesVersion = -1;
		
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
		generate();
		
		if (getVertices() == null || getTriangles() == null) {
			return;
		}
		
		ShaderProgram program = renderer.getProgram();
		Upload upload = uploads.get(program);
		if (upload == null || upload.version != getVersion()) {
			upload = upload(program, upload);
		}
		
		vertex.setColor(color);
		vertex.setTexture(texture);
		upload.setConstantAttributes(program);
		
		boolean bindTexture = texture != null && upload.textureIdLocation >= 0;
		if (bindTexture) {
			texture.bind();
		}
		
		program.use();
		upload.vertexArrayObject.bind();
		glDrawElements(GL_TRIANGLES, getTriangles().length, GL_UNSIGNED_INT, 0);
		renderer.increaseVertices(getTriangles().length);
		
		if (bindTexture) {
			texture.unbind();
		}
	}
	
	/**
	 * Uploads the data of this mesh to the GPU for a given shader program.
	 * @param upload The outdated upload to reuse, or {@code null} to create a new upload
	 * @return The updated upload.
	 */
	private Upload upload(ShaderProgram program, Upload upload) {
		float[] vertices = getVertices();
		int[] triangles = getTriangles();
		float[] normals = getNormals();
		float[] uvArray = getUVArray();
		
		if (upload == null) {
			upload = new Upload(new VertexArrayObject(), new VertexBufferObject());
			uploads.put(program, upload);
		}
		upload.version = getVersion();
		
		int totalVertices = vertices.length / 3;
		boolean hasNormals = normals != null && normals.length == vertices.length;
		
		Vertex vertex = new Vertex();
		vertex.setColor(Color.white());
		vertex.setTexture(null);
		
		FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(totalVertices * program.getStrideLength());
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
			
			upload.vertexArrayObject.bind();
			
			upload.vertexBufferObject.bind(VertexBufferObject.Target.ARRAY_BUFFER);
			upload.vertexBufferObject.uploadData(VertexBufferObject.Target.ARRAY_BUFFER, verticesBuffer, VertexBufferObject.Usage.STATIC_DRAW);
			program.bind();
			upload.disableConstantAttributes(program);
			
			uploadTriangles(triangles);
			trianglesBuffer.bind(VertexBufferObject.Target.ELEMENT_ARRAY_BUFFER);
		} finally {
			MemoryUtil.memFree(verticesBuffer);
		}
		
		return upload;
	}
	
	private void uploadTriangles(int[] triangles) {
		if (trianglesBuffer == null) {
			trianglesBuffer = new VertexBufferObject();
		}
		
		if (trianglesVersion == getVersion()) {
			return;
		}
		
		IntBuffer trianglesBuffer = MemoryUtil.memAllocInt(triangles.length);
		try {
			trianglesBuffer.put(triangles).flip();
			this.trianglesBuffer.bind(VertexBufferObject.Target.ELEMENT_ARRAY_BUFFER);
			this.trianglesBuffer.uploadData(VertexBufferObject.Target.ELEMENT_ARRAY_BUFFER, trianglesBuffer, VertexBufferObject.Usage.STATIC_DRAW);
			trianglesVersion = getVersion();
		} finally {
			MemoryUtil.memFree(trianglesBuffer);
		}
	}
	
	@Override
	public void destroy() {
		for (Upload upload : uploads.values()) {
			Destructible.destroy(upload.vertexArrayObject, upload.vertexBufferObject);
		}
		uploads.clear();
		trianglesBuffer = Destructible.destroy(trianglesBuffer);
		trianglesVersion = -1;
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
	
	private static class Upload {
		
		private final VertexArrayObject vertexArrayObject;
		private final VertexBufferObject vertexBufferObject;
		private int version;

		private int colorLocation = -1;
		private int textureIdLocation = -1;
		private int textureTypeLocation = -1;
		
		private Upload(VertexArrayObject vertexArrayObject, VertexBufferObject vertexBufferObject) {
			this.vertexArrayObject = vertexArrayObject;
			this.vertexBufferObject = vertexBufferObject;
		}
		
		private void disableConstantAttributes(ShaderProgram program) {
			colorLocation = disableAttribute(program, ShaderProgram.COLOR_ATTRIBUTE);
			textureIdLocation = disableAttribute(program, ShaderProgram.TEXTURE_ID_ATTRIBUTE);
			textureTypeLocation = disableAttribute(program, ShaderProgram.TEXTURE_TYPE_ATTRIBUTE);
		}
		
		private void setConstantAttributes(ShaderProgram program) {
			if (colorLocation >= 0) {
				program.setVertexAttributeValue(colorLocation, vertex.color);
			}
			if (textureIdLocation >= 0) {
				program.setVertexAttributeValue(textureIdLocation, vertex.textureId);
			}
			if (textureTypeLocation >= 0) {
				program.setVertexAttributeValue(textureTypeLocation, vertex.textureType);
			}
		}
		
		private static int disableAttribute(ShaderProgram program, String name) {
			int location = program.getAttributeLocation(name);
			if (location >= 0) {
				program.disableVertexAttribute(location);
			}
			return location;
		}
		
	}
	
}
