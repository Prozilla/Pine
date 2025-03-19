package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.system.resource.Color;

public class VariableColorProperty extends VariableProperty<Color> implements ColorProperty {
	
	protected VariableProperty<Float> red;
	protected VariableProperty<Float> green;
	protected VariableProperty<Float> blue;
	protected VariableProperty<Float> alpha;
	
	public VariableColorProperty(Color color) {
		this(new FixedProperty<>(color.getRed()), new FixedProperty<>(color.getGreen()), new FixedProperty<>(color.getBlue()), new FixedProperty<>(color.getAlpha()));
	}
	
	public VariableColorProperty(VariableProperty<Float> red, VariableProperty<Float> green, VariableProperty<Float> blue) {
		this(red, green, blue, new FixedProperty<>(1f));
	}
	
	public VariableColorProperty(VariableProperty<Float> red, VariableProperty<Float> green, VariableProperty<Float> blue, VariableProperty<Float> alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void setRed(VariableProperty<Float> red) {
		this.red = red;
	}
	
	public void setGreen(VariableProperty<Float> green) {
		this.green = green;
	}
	
	public void setBlue(VariableProperty<Float> blue) {
		this.blue = blue;
	}
	
	public void setAlpha(VariableProperty<Float> alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	@Override
	public void apply(Color outputColor) {
		outputColor.setRed(red.getValue());
		outputColor.setGreen(green.getValue());
		outputColor.setBlue(blue.getValue());
		outputColor.setAlpha(alpha.getValue());
	}
}
