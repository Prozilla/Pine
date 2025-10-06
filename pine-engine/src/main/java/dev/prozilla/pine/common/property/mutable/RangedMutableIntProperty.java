package dev.prozilla.pine.common.property.mutable;

import dev.prozilla.pine.common.property.selection.WrapMode;
import dev.prozilla.pine.common.util.checks.Checks;

public class RangedMutableIntProperty extends SimpleMutableIntProperty {
	
	protected int min, max;
	protected WrapMode wrapMode;
	
	public RangedMutableIntProperty(int min, int max, WrapMode wrapMode) {
		this(min, min, max, wrapMode);
	}
	
	public RangedMutableIntProperty(int initialValue, int min, int max, WrapMode wrapMode) {
		super(initialValue);
		this.min = min;
		this.max = max;
		this.wrapMode = Checks.isNotNull(wrapMode, "wrapMode");
	}
	
	@Override
	public boolean set(int value) {
		return super.set(wrapMode.transform(value, min, max));
	}
	
	public void setRange(int min, int max) {
		setMin(min);
		setMax(max);
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setWrapMode(WrapMode wrapMode) {
		this.wrapMode = Checks.isNotNull(wrapMode, "wrapMode");
	}
	
}
