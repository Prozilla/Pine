package dev.prozilla.pine.core.rendering.mesh;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.util.checks.Checks;

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
	private int sourceVersion;
	
	public CachedMesh(M source) {
		super(Checks.isNotNull(source, "source").getVertices(), source.getTriangles(), source.getNormals(), source.getUVArray(), source.getOrigin());
		this.source = source;
		sourceVersion = source.getVersion();
	}
	
	@Override
	protected float[] generateVertices() {
		float[] vertices = source.getVertices();
		sourceVersion = source.getVersion();
		return vertices;
	}
	
	@Override
	protected int[] generateTriangles() {
		return source.getTriangles();
	}
	
	@Override
	protected float[] generateNormals() {
		return source != null ? source.getNormals() : null;
	}
	
	@Override
	protected float[] generateUVs() {
		return source.getUVArray();
	}
	
	@Override
	public boolean isDirty() {
		return super.isDirty() || source.isDirty() || sourceVersion != source.getVersion();
	}
	
	public M getSource() {
		return source;
	}
	
	public void setSource(M source) {
		Checks.isNotNull(source, "source");
		if (this.source == source) {
			return;
		}
		this.source = source;
		markAsDirty();
	}
	
	@Override
	public void setOriginX(float x) {
		source.setOriginX(x);
	}
	
	@Override
	public void setOriginY(float y) {
		source.setOriginY(y);
	}
	
	@Override
	public void setOriginZ(float z) {
		source.setOriginZ(z);
	}
	
	@Override
	public void setOrigin(Vector3f origin) {
		source.setOrigin(origin);
	}
	
	@Override
	public void setSmoothShading(boolean smoothShading) {
		source.setSmoothShading(smoothShading);
	}
	
	@Override
	public void recalculateNormals() {
		if (source == null) {
			return;
		}
		source.recalculateNormals();
		markAsDirty();
	}
	
	@Override
	public CachedMesh<M> cached() {
		return this;
	}
	
}
