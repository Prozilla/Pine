package dev.prozilla.pine.common.property;

import dev.prozilla.pine.common.property.fixed.FixedFloatProperty;
import dev.prozilla.pine.common.system.Color;

public class VariableColorProperty implements ColorProperty {
	
	protected FloatProperty red;
	protected FloatProperty green;
	protected FloatProperty blue;
	protected FloatProperty alpha;
	
	public VariableColorProperty(Color color) {
		this(new FixedFloatProperty(color.getRed()), new FixedFloatProperty(color.getGreen()), new FixedFloatProperty(color.getBlue()), new FixedFloatProperty(color.getAlpha()));
	}
	
	public VariableColorProperty(FloatProperty red, FloatProperty green, FloatProperty blue) {
		this(red, green, blue, new FixedFloatProperty(1f));
	}
	
	public VariableColorProperty(FloatProperty red, FloatProperty green, FloatProperty blue, FloatProperty alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void setRed(FloatProperty red) {
		this.red = red;
	}
	
	public void setGreen(FloatProperty green) {
		this.green = green;
	}
	
	public void setBlue(FloatProperty blue) {
		this.blue = blue;
	}
	
	public void setAlpha(FloatProperty alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public Color getValue() {
		return getColor();
	}
	
	@Override
	public void transmit(Color target) {
		target.setRed(red.get());
		target.setGreen(green.get());
		target.setBlue(blue.get());
		target.setAlpha(alpha.get());
	}
}
