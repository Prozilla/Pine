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
	
	public abstract ModifiedMesh apply(float[] vertices, float[] uvArray, int[] triangles);
	
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
		public final float[] uvArray;
		public final int[] triangles;
		
		public ModifiedMesh(float[] vertices, float[] uvArray, int[] triangles) {
			this.vertices = vertices;
			this.uvArray = uvArray;
			this.triangles = triangles;
		}
	}
}
