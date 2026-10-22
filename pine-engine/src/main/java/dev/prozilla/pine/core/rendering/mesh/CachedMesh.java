package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Objects;

/**
 * An optimized static mesh that only gets uploaded to the GPU when its source gets changed.
 *
 * <p>
 *     Instead of being uploaded to the GPU every frame like normal meshes, or only once like static meshes, this mesh
 *     is uploaded to the GPU whenever changes are detected.
 * </p>
 * @param <M> The type of the source mesh.
 */
public class CachedMesh<M extends Mesh> extends StaticMesh {
	
	private M source;
	
	public CachedMesh(M source) {
		super(source.getVertices(), source.getTriangles(), source.getNormals(), source.getUVArray(), source.getOrigin().clone());
		this.source = source;
	}
	
	@Override
	protected float[] generateVertices() {
		return source.getVertices();
	}
	
	@Override
	protected int[] generateTriangles() {
		return source.getTriangles();
	}
	
	@Override
	protected float[] generateNormals() {
		return source.getNormals();
	}
	
	@Override
	protected float[] generateUVs() {
		return source.getUVArray();
	}
	
	@Override
	public boolean isDirty() {
		return super.isDirty() || source.isDirty();
	}
	
	public M getSource() {
		return source;
	}
	
	public void setSource(M source) {
		Checks.isNotNull(source, "source");
		if (Objects.equals(this.source, source)) {
			return;
		}
		this.source = source;
		markAsDirty();
	}
	
	@Override
	public CachedMesh<M> cached() {
		return this;
	}
	
}
