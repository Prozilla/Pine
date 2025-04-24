package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.core.component.ui.Node;

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
	public boolean isDirty(Node node, boolean isHorizontal) {
		return isDirty || dimensionA.isDirty(node, isHorizontal) || dimensionB.isDirty(node, isHorizontal);
	}
	
	@Override
	protected final float recompute(Node node, boolean isHorizontal) {
		isDirty = false;
		return compare(dimensionA.compute(node, isHorizontal), dimensionB.compute(node, isHorizontal));
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
	abstract protected float compare(float valueA, float valueB);
}
