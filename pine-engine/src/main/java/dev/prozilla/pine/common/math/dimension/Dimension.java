package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Dimension of a UI element, defined by the combination of a value and a unit or by a function that computes the value of a dimension.
 * The dimension system is based on the <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/length">CSS length data type</a>,
 * and has similar features and conventions.
 */
public class Dimension extends DimensionBase {

	protected int value;
	protected Unit unit;
	
	protected boolean isDirty;
	protected float currentFactor;
	
	public static final int DEFAULT_VALUE = 0;
	public static final Unit DEFAULT_UNIT = Unit.PIXELS;
	
	public Dimension() {
		this(DEFAULT_VALUE);
	}
	
	public Dimension(int value) {
		this(value, DEFAULT_UNIT);
	}
	
	public Dimension(int value, Unit unit) {
		this.value = value;
		this.unit = unit;
		isDirty = DEFAULT_DIRTY;
	}
	
	/**
	 * Computes the value of this dimension by multiplying the original value with a factor based on the unit.
	 */
	@Override
	protected int recompute(RectTransform context) {
		isDirty = false;
		
		if (value == 0 || currentFactor == 0) {
			return 0;
		}
		
		if (currentFactor == 1f) {
			return value;
		}
		
		return Math.round(value * currentFactor);
	}
	
	@Override
	public boolean isDirty(RectTransform context) {
		if (isDirty) {
			currentFactor = getFactor(context);
			return true;
		} else if (Unit.isFixed(unit)) {
			return false;
		}
		
		// Check if factor was changed
		float newFactor = getFactor(context);
		if (newFactor != currentFactor) {
			currentFactor = newFactor;
			return true;
		}
		
		return false;
	}
	
	public boolean equals(DimensionBase dimensionBase) {
		return dimensionBase instanceof Dimension dimension
				&& dimension.value == this.value && dimension.unit.equals(this.unit);
	}
	
	@Override
	public String toString() {
		if (unit == Unit.AUTO) {
			return Unit.toString(unit);
		} else {
			return value + Unit.toString(unit);
		}
	}
	
	/**
	 * Returns the factor based on this dimension's unit and a given context element.
	 */
	protected float getFactor(RectTransform context) {
		return switch (unit) {
			case AUTO -> 0f;
			case ELEMENT_SIZE -> 16f;
			case VIEWPORT_WIDTH -> context.getCanvas().getWidth() / 100f;
			case VIEWPORT_HEIGHT -> context.getCanvas().getHeight() / 100f;
			default -> 1f;
		};
	}
	
	public void set(int value, Unit unit) {
		setValue(value);
		setUnit(unit);
	}
	
	public void setValue(int value) {
		if (value == this.value) {
			return;
		}
		
		this.value = value;
		isDirty = true;
	}
	
	public void setUnit(Unit unit) {
		if (unit == this.unit) {
			return;
		}
		
		this.unit = unit;
		isDirty = true;
	}
	
	@Override
	public Dimension clone() {
		return new Dimension(value, unit);
	}
	
	/**
	 * Creates a dimension with value <code>0</code>.
	 */
	public static Dimension zero() {
		return new Dimension(0);
	}
	
	/**
	 * Creates a dimension that automatically computes, regardless of its value.
	 */
	public static Dimension auto() {
		return new Dimension(DEFAULT_VALUE, Unit.AUTO);
	}
	
	/**
	 * Creates a dimension based on the viewport width.
	 */
	public static Dimension viewportWidth() {
		return new Dimension(100, Unit.VIEWPORT_WIDTH);
	}
	
	/**
	 * Creates a dimension based on the viewport height.
	 */
	public static Dimension viewportHeight() {
		return new Dimension(100, Unit.VIEWPORT_HEIGHT);
	}
	
	/**
	 * Creates a dimension based on the highest value of two dimensions.
	 */
	public static Max max(DimensionBase dimensionA, DimensionBase dimensionB) {
		return new Max(dimensionA, dimensionB);
	}
	
	/**
	 * Creates a dimension based on the lowest value of two dimensions.
	 */
	public static Min min(DimensionBase dimensionA, DimensionBase dimensionB) {
		return new Min(dimensionA, dimensionB);
	}
	
	/**
	 * Creates a dimension based on the lowest value of two dimensions.
	 */
	public static Clamp clamp(DimensionBase dimension, DimensionBase dimensionMin, DimensionBase dimensionMax) {
		return new Clamp(dimension, dimensionMin, dimensionMax);
	}
	
	/**
	 * Creates a dimension based on a predicate.
	 */
	public static If predicate(boolean predicate, DimensionBase dimensionTrue, DimensionBase dimensionFalse) {
		return new If(predicate, dimensionTrue, dimensionFalse);
	}
	
	/**
	 * Creates a dimension based on the sum of the values of two dimensions.
	 */
	public static Add add(DimensionBase dimensionA, DimensionBase dimensionB) {
		return new Add(dimensionA, dimensionB);
	}
	
	/**
	 * Creates a dimension based on the product of the values of two dimensions.
	 */
	public static Multiply multiply(DimensionBase dimensionA, DimensionBase dimensionB) {
		return new Multiply(dimensionA, dimensionB);
	}
	
	// TO DO: Add static parse function to turn string into dimension
	
	public static class Max extends DimensionComparator {
		
		/**
		 * Creates a function that returns the highest value of two dimensions.
		 */
		public Max(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected int compare(int valueA, int valueB) {
			return Math.max(valueA, valueB);
		}
		
		@Override
		public Max clone() {
			return new Max(dimensionA, dimensionB);
		}
		
		@Override
		public String toString() {
			return String.format("max(%s, %s)", dimensionA, dimensionB);
		}
	}
	
	public static class Min extends DimensionComparator {
		
		/**
		 * Creates a function that returns the lowest value of two dimensions.
		 */
		public Min(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected int compare(int valueA, int valueB) {
			return Math.min(valueA, valueB);
		}
		
		@Override
		public Min clone() {
			return new Min(dimensionA, dimensionB);
		}
		
		@Override
		public String toString() {
			return String.format("min(%s, %s)", dimensionA, dimensionB);
		}
	}
	
	public static class If extends DimensionComparator {
		
		protected boolean predicate;
		
		/**
		 * Creates a function that returns value of <code>dimensionTrue</code> if predicate is <code>true</code>,
		 * otherwise returns value of <code>dimensionFalse</code>.
		 */
		public If(boolean predicate, DimensionBase dimensionTrue, DimensionBase dimensionFalse) {
			super(dimensionTrue, dimensionFalse);
			this.predicate = predicate;
		}
		
		@Override
		protected int compare(int valueA, int valueB) {
			return predicate ? valueA : valueB;
		}
		
		@Override
		public If clone() {
			return new If(predicate, dimensionA, dimensionB);
		}
		
		@Override
		public boolean equals(DimensionBase dimensionBase) {
			return super.equals(dimensionBase)
	                && dimensionBase instanceof If pred && pred.getPredicate() == this.predicate;
		}
		
		public boolean getPredicate() {
			return predicate;
		}
		
		public void setPredicate(boolean predicate) {
			if (predicate == this.predicate) {
				return;
			}
			
			this.predicate = predicate;
			isDirty = true;
		}
		
		@Override
		public String toString() {
			return String.format("(%s ? %s : %s)", predicate, dimensionA, dimensionB);
		}
	}
	
	public static class Add extends DimensionComparator {
		
		/**
		 * Creates a function that returns the sum of the values of two dimensions.
		 */
		public Add(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected int compare(int valueA, int valueB) {
			return valueA + valueB;
		}
		
		@Override
		public Add clone() {
			return new Add(dimensionA, dimensionB);
		}
		
		@Override
		public String toString() {
			return String.format("(%s + %s)", dimensionA, dimensionB);
		}
	}
	
	public static class Multiply extends DimensionComparator {
		
		/**
		 * Creates a function that returns the product of the values of two dimensions.
		 */
		public Multiply(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected int compare(int valueA, int valueB) {
			return valueA * valueB;
		}
		
		@Override
		public Multiply clone() {
			return new Multiply(dimensionA, dimensionB);
		}
		
		@Override
		public String toString() {
			return String.format("(%s * %s)", dimensionA, dimensionB);
		}
	}
	
	public static class Clamp extends DimensionBase {
		
		private final DimensionBase dimension;
		private final DimensionBase dimensionMin;
		private final DimensionBase dimensionMax;
		
		/**
		 * Creates a function that returns the value of a dimension, clamped between the values of two dimensions.
		 */
		public Clamp(DimensionBase dimension, DimensionBase dimensionMin, DimensionBase dimensionMax) {
			this.dimension = dimension;
			this.dimensionMin = dimensionMin;
			this.dimensionMax = dimensionMax;
		}
		
		@Override
		public boolean isDirty(RectTransform context) {
			return dimension.isDirty(context) || dimensionMin.isDirty(context) || dimensionMax.isDirty(context);
		}
		
		@Override
		protected int recompute(RectTransform context) {
			int value = dimension.compute(context);
			int min = dimensionMin.compute(context);
			
			if (value <= min) {
				return min;
			}
			
			int max = dimensionMax.compute(context);
			
			return Math.min(value, max);
		}
		
		@Override
		public Clamp clone() {
			return new Clamp(dimension, dimensionMin, dimensionMax);
		}
		
		@Override
		public boolean equals(DimensionBase dimensionBase) {
			return dimensionBase instanceof Clamp clamp && clamp.equals(this.dimension, this.dimensionMin, this.dimensionMax);
		}
		
		public boolean equals(DimensionBase dimension, DimensionBase dimensionMin, DimensionBase dimensionMax) {
			return this.dimension.equals(dimension) && this.dimensionMin.equals(dimensionMin) && this.dimensionMax.equals(dimensionMax);
		}
		
		@Override
		public String toString() {
			return String.format("clamp(%s, %s, %s)", dimensionMin, dimension, dimensionMax);
		}
	}
}
