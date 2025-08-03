package dev.prozilla.pine.core.rendering.shape.modifier;

import dev.prozilla.pine.common.lifecycle.Destructible;
import dev.prozilla.pine.core.rendering.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public abstract class ShapeModifier implements Destructible {
	
	private final List<Shape> targets;
	
	public ShapeModifier() {
		targets = new ArrayList<>();
	}
	
	public abstract float[] modifyVertices(float[] vertices);
	
	public abstract float[] modifyUVs(float[] oldVertices, float[] newVertices, float[] uvArray);

	protected void markAsDirty() {
		for (Shape target : targets) {
			target.isDirty = true;
		}
	}
	
	public void addTarget(Shape target) {
		targets.add(target);
	}
	
	public void removeTarget(Shape target) {
		targets.remove(target);
	}
	
	@Override
	public void destroy() {
		List<Shape> targets = new ArrayList<>(this.targets);
		this.targets.clear();
		for (Shape target : targets) {
			target.removeModifier(this);
		}
	}
	
}
