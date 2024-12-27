package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.canvas.RectTransform;

/**
 * Base class for dimensions of UI elements.
 */
public abstract class DimensionBase implements Printable {
	
	protected int computedValue;
	
	public static final boolean DEFAULT_DIRTY = true;
	
	/**
	 * Calculates the value of this dimension in the context of a given UI element.
	 * @param context UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	public final int compute(RectTransform context) {
		if (!isDirty(context)) {
			return computedValue;
		}
		
		computedValue = recompute(context);
		return computedValue;
	}
	
	/**
	 * Recalculates the value of this dimension when it has been marked as dirty.
	 * @param context UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	abstract protected int recompute(RectTransform context);
	
	/**
	 * Checks whether this dimension has been modified since the last calculation.
	 * @return True if this dimension has been modified.
	 */
	public boolean isDirty(RectTransform context) {
		return DEFAULT_DIRTY;
	}
	
	/**
	 * Returns a clone of this dimension.
	 * @return New dimension instance
	 */
	@Override
	abstract public DimensionBase clone();
	
	/**
	 * Checks whether this dimension is equal to a given dimension.
	 * @param dimensionBase Other dimension
	 */
	abstract public boolean equals(DimensionBase dimensionBase);
	
	/**
	 * Returns the string representation of this dimension.
	 */
	abstract public String toString();
}
