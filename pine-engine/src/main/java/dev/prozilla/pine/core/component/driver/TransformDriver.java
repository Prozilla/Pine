package dev.prozilla.pine.core.component.driver;

import dev.prozilla.pine.common.property.adaptive.AdaptiveVector3fProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;

public class TransformDriver extends Driver {

	protected AdaptiveVector3fProperty positionProperty;
	protected AdaptiveVector3fProperty rotationProperty;
	protected AdaptiveVector3fProperty scaleProperty;
	
	public TransformDriver(AnimationData animationData) {
		super(animationData);
	}
	
	public AdaptiveVector3fProperty getPositionProperty() {
		return positionProperty;
	}
	
	public void setPositionProperty(AdaptiveVector3fProperty positionProperty) {
		replaceProperty(this.positionProperty, positionProperty);
		this.positionProperty = positionProperty;
	}
	
	public AdaptiveVector3fProperty getRotationProperty() {
		return rotationProperty;
	}
	
	public void setRotationProperty(AdaptiveVector3fProperty rotationProperty) {
		replaceProperty(this.rotationProperty, rotationProperty);
		this.rotationProperty = rotationProperty;
	}
	
	public AdaptiveVector3fProperty getScaleProperty() {
		return scaleProperty;
	}
	
	public void setScaleProperty(AdaptiveVector3fProperty scaleProperty) {
		replaceProperty(this.scaleProperty, scaleProperty);
		this.scaleProperty = scaleProperty;
	}
}
