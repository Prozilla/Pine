package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.core.component.ui.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Represents four dimensions (X, Y, Z and W) for a UI element. Each dimension is defined using a {@link DimensionBase}.
 * @see Dimension
 */
public class QuadDimension extends DualDimension {
	
	public DimensionBase z;
	public DimensionBase w;
	
	/**
	 * Creates four default dimensions with all values set to 0 pixels.
	 */
	public QuadDimension() {
		this(DEFAULT_VALUE);
	}
	
	public QuadDimension(int xyzw) {
		this(new Dimension(xyzw));
	}
	
	/**
	 * Creates four dimensions with equal values.
	 */
	public QuadDimension(DimensionBase xyzw) {
		this(xyzw, xyzw);
	}
	
	public QuadDimension(int xz, int yw) {
		this(new Dimension(xz), new Dimension(yw));
	}
	
	/**
	 * Creates four dimensions with two pairs of equal values.
	 * The X- and Z-dimensions and the Y- and W-dimensions will respectively be assigned equal values.
	 */
	public QuadDimension(DimensionBase xz, DimensionBase yw) {
		this(xz, yw, xz, yw);
	}
	
	public QuadDimension(int x, int y, int z, int w) {
		this(new Dimension(x), new Dimension(y), new Dimension(z), new Dimension(w));
	}
	
	public QuadDimension(DimensionBase x, DimensionBase y, DimensionBase z, DimensionBase w) {
		super(x, y);
		this.z = z;
		this.w = w;
	}
	
	@Override
	public void set(int xyzw) {
		set(new Dimension(xyzw));
	}
	
	@Override
	public void set(DimensionBase xyzw) {
		set(xyzw, xyzw);
	}
	
	@Override
	public void set(int xz, int yw) {
		set(new Dimension(xz), new Dimension(yw));
	}
	
	@Override
	public void set(DimensionBase xz, DimensionBase yw) {
		set(xz, yw, xz, yw);
	}
	
	public void set(int x, int y, int z, int w) {
		set(new Dimension(x), new Dimension(y), new Dimension(z), new Dimension(w));
	}
	
	public void set(DimensionBase x, DimensionBase y, DimensionBase z, DimensionBase w) {
		if (!x.equals(this.x)) {
			this.x = x;
		}
		if (!y.equals(this.y)) {
			this.y = y;
		}
		if (!z.equals(this.z)) {
			this.z = z;
		}
		if (!w.equals(this.w)) {
			this.w = w;
		}
	}
	
	/**
	 * Computes the Z-dimension for a given element.
	 */
	public float computeZ(Node element) {
		return z.compute(element, true);
	}
	
	/**
	 * Computes the W-dimension for a given element.
	 */
	public float computeW(Node element) {
		return w.compute(element, false);
	}
	
	/**
	 * Computes the sum of the X- and Z-dimensions for a given element.
	 */
	public float computeXZ(Node element) {
		return computeX(element) + computeZ(element);
	}
	
	/**
	 * Computes the sum of the Y- and W-dimensions for a given element.
	 */
	public float computeYW(Node element) {
		return computeY(element) + computeW(element);
	}
	
	@Override
	public boolean isZero(Node element) {
		return super.isZero(element) || computeZ(element) == 0 || computeW(element) == 0;
	}
	
	@Override
	public QuadDimension clone() {
		return new QuadDimension(x.clone(), y.clone(), z.clone(), w.clone());
	}
	
	@Override
	public @NotNull String toString() {
		return String.format("(%s,%s,%s,%s)", x, y, z, w);
	}
}
