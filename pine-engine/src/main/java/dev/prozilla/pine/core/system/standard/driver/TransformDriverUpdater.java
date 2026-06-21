package dev.prozilla.pine.core.system.standard.driver;

import dev.prozilla.pine.common.math.vector.Vector3f;
import dev.prozilla.pine.common.property.Property;
import dev.prozilla.pine.core.component.Transform;
import dev.prozilla.pine.core.component.driver.TransformDriver;
import dev.prozilla.pine.core.entity.EntityChunk;
import dev.prozilla.pine.core.system.update.UpdateSystem;

public class TransformDriverUpdater extends UpdateSystem {
	
	public TransformDriverUpdater() {
		super(TransformDriver.class);
	}
	
	@Override
	protected void process(EntityChunk chunk, float deltaTime) {
		Transform transform = chunk.getTransform();
		TransformDriver driver = chunk.getComponent(TransformDriver.class);
		
		Vector3f position = Property.getValueOf(driver.getPositionProperty());
		if (position != null) {
			transform.setPosition(position);
		}
		
		Vector3f rotation = Property.getValueOf(driver.getRotationProperty());
		if (rotation != null) {
			transform.setRotation(rotation);
		}
		
		Vector3f scale = Property.getValueOf(driver.getScaleProperty());
		if (scale != null) {
			transform.setScale(scale);
		}
	}
}
