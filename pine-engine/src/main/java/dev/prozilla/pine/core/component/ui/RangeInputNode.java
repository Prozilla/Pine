package dev.prozilla.pine.core.component.ui;

import dev.prozilla.pine.common.math.MathUtils;
import dev.prozilla.pine.common.property.FloatProperty;
import dev.prozilla.pine.common.property.bindable.BindableFloatProperty;
import dev.prozilla.pine.common.property.bindable.SimpleBindableFloatProperty;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.Component;

public class RangeInputNode extends Component {
	
	private BindableFloatProperty valueProperty;
	
	private float min;
	private float max;
	public float step;
	
	public Node progressNode;
	public Node thumbNode;
	public Node trackNode;
	
	public boolean isDragging;
	
	public static final float DEFAULT_MIN = 0;
	public static final float DEFAULT_MAX = 100;
	public static final float DEFAULT_STEP = 1;
	
	public static final String PROGRESS_ELEMENT = "range-progress";
	public static final String THUMB_ELEMENT = "range-thumb";
	public static final String TRACK_ELEMENT = "range-track";
	
	public RangeInputNode() {
		this(DEFAULT_MIN, DEFAULT_MAX);
	}
	
	public RangeInputNode(float min, float max) {
		this(min, max, DEFAULT_STEP);
	}
	
	public RangeInputNode(float min, float max, float step) {
		this(min, max, step, (min + max) / 2f);
	}
	
	public RangeInputNode(float min, float max, float step, float value) {
		this(min, max, step, new SimpleBindableFloatProperty(value));
	}
	
	public RangeInputNode(BindableFloatProperty valueProperty) {
		this(DEFAULT_MIN, DEFAULT_MAX, valueProperty);
	}
	
	public RangeInputNode(float min, float max, BindableFloatProperty valueProperty) {
		this(min, max, DEFAULT_STEP, valueProperty);
	}
	
	public RangeInputNode(float min, float max, float step, BindableFloatProperty valueProperty) {
		this.min = min;
		this.max = max;
		this.step = step;
		setValueProperty(valueProperty);
	}
	
	public BindableFloatProperty getValueProperty() {
		return valueProperty;
	}
	
	public void setValueProperty(BindableFloatProperty valueProperty) {
		Checks.isNotNull(valueProperty, "valueProperty");
		if (this.valueProperty != null) {
			this.valueProperty.removeObserver(this::handleValueChange);
		}
		this.valueProperty = valueProperty;
		this.valueProperty.read(this::handleValueChange);
	}
	
	public float getMin() {
		return min;
	}
	
	public float getMax() {
		return max;
	}
	
	public void setMin(float min) {
		if (this.min == min) {
			return;
		}
		this.min = min;
		handleValueChange(valueProperty.get());
	}
	
	public void setMax(float max) {
		if (this.max == max) {
			return;
		}
		this.max = max;
		handleValueChange(valueProperty.get());
	}
	
	private void handleValueChange(float value) {
		valueProperty.set(MathUtils.clamp(value, min, max));
	}
	
	public FloatProperty progressProperty() {
		return this::getProgress;
	}
	
	public float getProgress() {
		return MathUtils.remap(getValue(), min, max, 0, 1);
	}
	
	public float getValue() {
		return valueProperty.get();
	}
	
	public void setValue(float value) {
		valueProperty.set(value);
	}
	
}
