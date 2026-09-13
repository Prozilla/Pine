package dev.prozilla.pine.core.component.driver;

import dev.prozilla.pine.common.property.adaptive.AdaptiveVector3fProperty;
import dev.prozilla.pine.core.component.animation.AnimationData;

public class TransformDriver extends Driver {

	protected AdaptiveVector3fProperty positionProperty;
	protected AdaptiveVector3fProperty rotationProperty;
	protected AdaptiveVector3fProperty scaleProperty;
	protected AdaptiveVector3fProperty originProperty;
	
	public TransformDriver(AnimationData animationData) {
		super(animationData);
	}
	
	public AdaptiveVector3fProperty getPositionProperty() {
		return positionProperty;
	}
	
	public void setPositionProperty(AdaptiveVector3fProperty positionProperty) {
		this.positionProperty = replaceProperty(this.positionProperty, positionProperty);
	}
	
	public AdaptiveVector3fProperty getRotationProperty() {
		return rotationProperty;
	}
	
	public void setRotationProperty(AdaptiveVector3fProperty rotationProperty) {
		this.rotationProperty = replaceProperty(this.rotationProperty, rotationProperty);
	}
	
	public AdaptiveVector3fProperty getScaleProperty() {
		return scaleProperty;
	}
	
	public void setScaleProperty(AdaptiveVector3fProperty scaleProperty) {
		this.scaleProperty = replaceProperty(this.scaleProperty, scaleProperty);
	}
	
	public AdaptiveVector3fProperty getOriginProperty() {
		return originProperty;
	}
	
	public void setOriginProperty(AdaptiveVector3fProperty originProperty) {
		this.originProperty = replaceProperty(this.originProperty, originProperty);
	}
}
