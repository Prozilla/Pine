package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Base class for dimensions of UI elements.
 */
public abstract class DimensionBase implements Printable, Cloneable<DimensionBase> {
	
	protected int computedValue;
	
	public static final boolean DEFAULT_DIRTY = true;
	
	/**
	 * Calculates the value of this dimension in the context of a given UI element.
	 * @param context UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	public final int compute(RectTransform context, boolean isHorizontal) {
		if (!isDirty(context, isHorizontal)) {
			return computedValue;
		}
		
		computedValue = recompute(context, isHorizontal);
		return computedValue;
	}
	
	/**
	 * Recalculates the value of this dimension when it has been marked as dirty.
	 * @param context UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	abstract protected int recompute(RectTransform context, boolean isHorizontal);
	
	/**
	 * Checks whether this dimension has been modified since the last calculation.
	 * @return True if this dimension has been modified.
	 */
	public boolean isDirty(RectTransform context, boolean isHorizontal) {
		return DEFAULT_DIRTY;
	}
	
	public Unit getUnit() {
		return null;
	}
	
	/**
	 * Returns a clone of this dimension.
	 * @return New dimension instance
	 */
	@Override
	abstract public DimensionBase clone();
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		
		return (object instanceof DimensionBase dimensionBase) ? equals(dimensionBase) : super.equals(object);
	}
	
	/**
	 * Checks whether this dimension is equal to a given dimension.
	 * @param dimensionBase Other dimension
	 */
	abstract public boolean equals(DimensionBase dimensionBase);
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
	 * Returns the string representation of this dimension.
	 */
	abstract public String toString();
}
