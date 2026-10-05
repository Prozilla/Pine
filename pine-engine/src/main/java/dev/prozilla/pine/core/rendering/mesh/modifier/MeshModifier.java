package dev.prozilla.pine.core.rendering.mesh.modifier;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.rendering.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

public abstract class MeshModifier implements Destructible {
	
	private final List<Mesh> targets;
	
	public MeshModifier() {
		targets = new ArrayList<>();
	}
	
	public abstract ModifiedMesh apply(float[] vertices, int[] triangles, float[] normals, float[] uvArray);
	
	protected void markAsDirty() {
		for (Mesh target : targets) {
			target.markAsDirty();
		}
	}
	
	public void addTarget(Mesh target) {
		targets.add(target);
	}
	
	public void removeTarget(Mesh target) {
		targets.remove(target);
	}
	
	@Override
	public void destroy() {
		List<Mesh> targets = new ArrayList<>(this.targets);
		this.targets.clear();
		for (Mesh target : targets) {
			target.removeModifier(this);
		}
	}
	
	public static class ModifiedMesh {
		public final float[] vertices;
		public final int[] triangles;
		public final float[] normals;
		public final float[] uvArray;
		
		public ModifiedMesh(float[] vertices, int[] triangles, float[] normals, float[] uvArray) {
			this.vertices = vertices;
			this.triangles = triangles;
			this.normals = normals;
			this.uvArray = uvArray;
		}
	}
}
