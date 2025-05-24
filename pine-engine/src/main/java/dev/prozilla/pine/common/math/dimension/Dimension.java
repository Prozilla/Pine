package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.exception.InvalidArrayException;
import dev.prozilla.pine.common.util.checks.Checks;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.function.BiFunction;

/**
 * Dimension of a UI element, defined by the combination of a value and a unit or by a function that computes the value of a dimension.
 * The dimension system is based on the <a href="https://developer.mozilla.org/en-US/docs/Web/CSS/length">CSS length data type</a>,
 * and has similar features and conventions.
 */
public class Dimension extends DimensionBase {

	protected float value;
	protected Unit unit;
	
	protected boolean isDirty;
	protected float currentFactor;
	
	public static final float DEFAULT_VALUE = 0;
	public static final Unit DEFAULT_UNIT = Unit.PIXELS;
	
	public Dimension() {
		this(DEFAULT_VALUE);
	}
	
	public Dimension(float value) {
		this(value, DEFAULT_UNIT);
	}
	
	public Dimension(float value, Unit unit) {
		this.value = value;
		this.unit = unit;
		isDirty = DEFAULT_DIRTY;
	}
	
	/**
	 * Computes the value of this dimension by multiplying the original value with a factor based on the unit.
	 */
	@Override
	protected float recompute(Node node, boolean isHorizontal) {
		isDirty = false;
		
		if (value == 0 || currentFactor == 0) {
			return 0;
		}
		
		if (currentFactor == 1f) {
			return value;
		}
		
		return value * currentFactor;
	}
	
	@Override
	public boolean isDirty(Node node, boolean isHorizontal) {
		if (isDirty) {
			currentFactor = getFactor(node, isHorizontal);
			return true;
		} else if (Unit.isFixed(unit)) {
			return false;
		}
		
		// Check if factor was changed
		float newFactor = getFactor(node, isHorizontal);
		if (newFactor != currentFactor) {
			currentFactor = newFactor;
			return true;
		}
		
		return false;
	}
	
	@Override
	public Unit getUnit() {
		return unit;
	}
	
	@Override
	public boolean equals(DimensionBase dimensionBase) {
		if (dimensionBase == this) {
			return true;
		}
		
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
	protected float getFactor(Node node, boolean isHorizontal) {
		return switch (unit) {
			case AUTO -> 0f;
			case ELEMENT_SIZE -> 16f;
			case VIEWPORT_WIDTH -> node.getRoot().getWidth() / 100f;
			case VIEWPORT_HEIGHT -> node.getRoot().getHeight() / 100f;
			case PERCENTAGE -> (isHorizontal ? node.getContext().getWidth() : node.getContext().getHeight()) / 100f;
			default -> 1f;
		};
	}
	
	public void set(float value, Unit unit) {
		setValue(value);
		setUnit(unit);
	}
	
	public void setValue(float value) {
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
	
	public static boolean isValid(String input) {
		try {
			parse(input);
			return true;
		} catch (RuntimeException e) {
			return false;
		}
	}
	
	/**
	 * Parses a string into a dimension instance.
	 * @param input Input string
	 * @return New dimension with based on input string
	 * @throws IllegalArgumentException When <code>input</code> is not a valid dimension string
	 * @deprecated Replaced by {@link DimensionParser} as of 1.2.0
	 */
	@Deprecated
	public static DimensionBase parse(String input) throws IllegalArgumentException {
		return new DimensionParser().read(input);
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
	
	public static Dimension parentSize() {
		return new Dimension(100, Unit.PERCENTAGE);
	}
	
	/**
	 * Creates a dimension based on the highest value of two or more dimensions.
	 */
	public static Max max(DimensionBase ...dimensions) throws InvalidArrayException {
		return chainDimensions(Max::new, dimensions);
	}
	
	/**
	 * Creates a dimension based on the lowest value of two or more dimensions.
	 */
	public static Min min(DimensionBase ...dimensions) throws InvalidArrayException {
		return chainDimensions(Min::new, dimensions);
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
	 * Creates a dimension based on the sum of the values of two or more dimensions.
	 */
	public static Add add(DimensionBase ...dimensions) throws InvalidArrayException {
		return chainDimensions(Add::new, dimensions);
	}
	
	/**
	 * Creates a dimension based on the difference of the values of two or more dimensions.
	 */
	public static Subtract subtract(DimensionBase ...dimensions) throws InvalidArrayException {
		return chainDimensions(Subtract::new, dimensions);
	}
	
	/**
	 * Creates a dimension based on the product of the values of two or more dimensions.
	 */
	public static Multiply multiply(DimensionBase ...dimensions) throws InvalidArrayException {
		return chainDimensions(Multiply::new, dimensions);
	}
	
	/**
	 * Helper method to chain dimensions based on the given constructor.
	 * @param constructor A constructor that creates a part of the dimension chain
	 * @param dimensions The dimensions to create a chain of
	 * @return The chain of dimensions
	 * @param <D> The type of the chain dimension
	 * @throws InvalidArrayException If less than two dimensions are passed
	 */
	private static <D extends DimensionBase> D chainDimensions(BiFunction<DimensionBase, DimensionBase, D> constructor, DimensionBase[] dimensions) throws InvalidArrayException {
		Checks.hasMinLength(dimensions, 2);
		
		// Start with the first two dimensions
		D result = constructor.apply(dimensions[0], dimensions[1]);
		
		// Iterate over the remaining dimensions and create chain
		for (int i = 2; i < dimensions.length; i++) {
			result = constructor.apply(result, dimensions[i]);
		}
		
		return result;
	}
	
	public static class Max extends DimensionComparator {
		
		/**
		 * Creates a function that returns the highest value of two dimensions.
		 */
		public Max(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected float compare(float valueA, float valueB) {
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
		protected float compare(float valueA, float valueB) {
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
		protected float compare(float valueA, float valueB) {
			return predicate ? valueA : valueB;
		}
		
		@Override
		public If clone() {
			return new If(predicate, dimensionA, dimensionB);
		}
		
		@Override
		public boolean equals(DimensionBase dimensionBase) {
			return super.equals(dimensionBase)
	                && dimensionBase instanceof If predicate && predicate.getPredicate() == this.predicate;
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
		protected float compare(float valueA, float valueB) {
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
	
	public static class Subtract extends DimensionComparator {
		
		/**
		 * Creates a function that returns the difference of the values of two dimensions.
		 */
		public Subtract(DimensionBase dimensionA, DimensionBase dimensionB) {
			super(dimensionA, dimensionB);
		}
		
		@Override
		protected float compare(float valueA, float valueB) {
			return valueA - valueB;
		}
		
		@Override
		public Subtract clone() {
			return new Subtract(dimensionA, dimensionB);
		}
		
		@Override
		public String toString() {
			return String.format("(%s - %s)", dimensionA, dimensionB);
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
		protected float compare(float valueA, float valueB) {
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
		public boolean isDirty(Node node, boolean isHorizontal) {
			return dimension.isDirty(node, isHorizontal) || dimensionMin.isDirty(node, isHorizontal) || dimensionMax.isDirty(node, isHorizontal);
		}
		
		@Override
		protected float recompute(Node node, boolean isHorizontal) {
			float value = dimension.compute(node, isHorizontal);
			float min = dimensionMin.compute(node, isHorizontal);
			
			if (value <= min) {
				return min;
			}
			
			float max = dimensionMax.compute(node, isHorizontal);
			
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
	
	public static class Mix extends DimensionComparator {
		
		protected float factor;
		
		public Mix(DimensionBase dimensionA, DimensionBase dimensionB) {
			this(dimensionA, dimensionB, 0);
		}
		
		/**
		 * Creates a function that returns the mix of the values of two dimensions based on a factor.
		 */
		public Mix(DimensionBase dimensionA, DimensionBase dimensionB, float factor) {
			super(dimensionA, dimensionB);
			this.factor = factor;
		}
		
		@Override
		protected float compare(float valueA, float valueB) {
			return valueA * (1 - factor) + valueB * factor;
		}
		
		@Override
		public Mix clone() {
			return new Mix(dimensionA, dimensionB, factor);
		}
		
		public float getFactor() {
			return factor;
		}
		
		public void setFactor(float factor) {
			if (this.factor == factor) {
				return;
			}
			
			this.factor = factor;
			isDirty = true;
		}
		
		@Override
		public String toString() {
			return String.format("mix(%s, %s, %s)", dimensionA, dimensionB, factor);
		}
	}
}
