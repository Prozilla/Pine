package dev.prozilla.pine.core.entity.prefab.ui;

import dev.prozilla.pine.common.property.bindable.BindableFloatProperty;
import dev.prozilla.pine.core.component.ui.RangeInputNode;
import dev.prozilla.pine.core.entity.Entity;

public class RangeInputPrefab extends NodePrefab {
	
	protected BindableFloatProperty valueProperty;
	protected float min;
	protected float max;
	protected float step;
	
	public RangeInputPrefab() {
		setName("RangeInput");
		setHTMLTag("input");
		setTabIndex(0);
		
		min = RangeInputNode.DEFAULT_MIN;
		max = RangeInputNode.DEFAULT_MAX;
		step = RangeInputNode.DEFAULT_STEP;
	}
	
	public void setMin(float min) {
		this.min = min;
	}
	
	public void setMax(float max) {
		this.max = max;
	}
	
	public void setStep(float step) {
		this.step = step;
	}
	
	public void setValueProperty(BindableFloatProperty valueProperty) {
		this.valueProperty = valueProperty;
	}
	
	@Override
	protected void apply(Entity entity) {
		super.apply(entity);
		
		RangeInputNode rangeInputNode = valueProperty == null ? new RangeInputNode(min, max, step) : new RangeInputNode(min, max, step, valueProperty);
		entity.addComponent(rangeInputNode);
	}
	
}
