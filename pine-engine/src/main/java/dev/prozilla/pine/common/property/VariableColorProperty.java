package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.system.Color;

public class VariableColorProperty implements ColorProperty {
	
	protected Property<Float> red;
	protected Property<Float> green;
	protected Property<Float> blue;
	protected Property<Float> alpha;
	
	public VariableColorProperty(Color color) {
		this(new FixedProperty<>(color.getRed()), new FixedProperty<>(color.getGreen()), new FixedProperty<>(color.getBlue()), new FixedProperty<>(color.getAlpha()));
	}
	
	public VariableColorProperty(Property<Float> red, Property<Float> green, Property<Float> blue) {
		this(red, green, blue, new FixedProperty<>(1f));
	}
	
	public VariableColorProperty(Property<Float> red, Property<Float> green, Property<Float> blue, Property<Float> alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void setRed(Property<Float> red) {
		this.red = red;
	}
	
	public void setGreen(Property<Float> green) {
		this.green = green;
	}
	
	public void setBlue(Property<Float> blue) {
		this.blue = blue;
	}
	
	public void setAlpha(Property<Float> alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	@Override
	public void transmit(Color target) {
		target.setRed(red.getValue());
		target.setGreen(green.getValue());
		target.setBlue(blue.getValue());
		target.setAlpha(alpha.getValue());
	}
}
