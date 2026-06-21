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
	
	public abstract float[] modifyVertices(float[] vertices);
	
	public abstract float[] modifyUVs(float[] oldVertices, float[] newVertices, float[] uvArray);

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
	
}
