package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Abstract class for a function that compares two dimensions.
 */
public abstract class DimensionComparator extends DimensionBase {
	
	protected final DimensionBase dimensionA;
	protected final DimensionBase dimensionB;
	protected boolean isDirty;
	
	public DimensionComparator(DimensionBase dimensionA, DimensionBase dimensionB) {
		this.dimensionA = dimensionA;
		this.dimensionB = dimensionB;
		isDirty = DEFAULT_DIRTY;
	}
	
	@Override
	public boolean isDirty(RectTransform context, boolean isHorizontal) {
		return isDirty || dimensionA.isDirty(context, isHorizontal) || dimensionB.isDirty(context, isHorizontal);
	}
	
	@Override
	protected final int recompute(RectTransform context, boolean isHorizontal) {
		isDirty = false;
		return compare(dimensionA.compute(context, isHorizontal), dimensionB.compute(context, isHorizontal));
	}
	
	public boolean has(DimensionBase dimension) {
		return dimensionA.equals(dimension) || dimensionB.equals(dimension);
	}
	
	@Override
	public boolean equals(DimensionBase dimensionBase) {
		return dimensionBase instanceof DimensionComparator comparator
                && comparator.has(this.dimensionA) && comparator.has(this.dimensionB);
	}
	
	/**
	 * Compares the values of both dimensions stored in this comparator.
	 * @return The result of this comparator function.
	 */
	abstract protected int compare(int valueA, int valueB);
}
