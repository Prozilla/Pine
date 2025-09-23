package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for dimensions of UI elements.
 */
public abstract class DimensionBase implements Printable, Cloneable<DimensionBase> {
	
	protected float computedValue;
	
	public static final boolean DEFAULT_DIRTY = true;
	
	/**
	 * Calculates the value of this dimension in the context of a given UI element.
	 * @param node UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	public final float compute(Node node, boolean isHorizontal) {
		if (!isDirty(node, isHorizontal)) {
			return computedValue;
		}
		
		computedValue = recompute(node, isHorizontal);
		return computedValue;
	}
	
	/**
	 * Recalculates the value of this dimension when it has been marked as dirty.
	 * @param node UI element, serving as the context of this dimension.
	 * @return The computed value of this dimension in pixels.
	 */
	abstract protected float recompute(Node node, boolean isHorizontal);
	
	/**
	 * Checks whether this dimension has been modified since the last calculation.
	 * @return True if this dimension has been modified.
	 */
	public boolean isDirty(Node node, boolean isHorizontal) {
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
		return object == this || (object instanceof DimensionBase dimensionBase && equals(dimensionBase));
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
	abstract public @NotNull String toString();
}
