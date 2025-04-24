package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.Cloneable;
import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.ui.Node;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a pair of dimensions (X and Y) for a UI element. Each dimension is defined using a {@link DimensionBase}.
 * @see Dimension
 */
public class DualDimension implements Printable, Cloneable<DualDimension> {
	
	public DimensionBase x;
	public DimensionBase y;
	
	public static final int DEFAULT_VALUE = 0;
	
	/**
	 * Creates a default pair of dimensions with all values set to 0 pixels.
	 */
	public DualDimension() {
		this(DEFAULT_VALUE);
	}
	
	/**
	 * Creates a pair of dimensions with two equal values based on a string.
	 */
	public DualDimension(String xy) {
		this(Dimension.parse(xy));
	}
	
	/**
	 * Creates a pair of dimensions with two equal values, in pixels.
	 */
	public DualDimension(int xy) {
		this(new Dimension(xy));
	}
	
	/**
	 * Creates a pair of dimensions with two equal values.
	 */
	public DualDimension(DimensionBase xy) {
		this(xy, xy);
	}
	
	/**
	 * Creates a pair of dimensions based on two strings.
	 */
	public DualDimension(String x, String y) {
		this(Dimension.parse(x), Dimension.parse(y));
	}
	
	/**
	 * Creates a pair of dimensions.
	 * @param x Value of x dimension, in pixels
	 * @param y Value of y dimension, in pixels
	 */
	public DualDimension(int x, int y) {
		this(new Dimension(x), new Dimension(y));
	}
	
	/**
	 * Creates a pair of dimensions
	 */
	public DualDimension(DimensionBase x, DimensionBase y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		set(DEFAULT_VALUE);
	}
	
	public void set(String xy) {
		set(Dimension.parse(xy));
	}
	
	public void set(int xy) {
		set(new Dimension(xy));
	}
	
	public void set(DimensionBase xy) {
		set(xy, xy);
	}
	
	public void set(String x, String y) {
		set(Dimension.parse(x), Dimension.parse(y));
	}
	
	public void set(int x, int y) {
		set(new Dimension(x), new Dimension(y));
	}
	
	public void set(DimensionBase x, DimensionBase y) {
		Objects.requireNonNull(x, "Dimension must not be null.");
		Objects.requireNonNull(y, "Dimension must not be null.");
		
		if (!x.equals(this.x)) {
			this.x = x;
		}
		if (!y.equals(this.y)) {
			this.y = y;
		}
	}
	
	/**
	 * Computes the X-dimension for a given element.
	 */
	public float computeX(Node element) {
		return x.compute(element, true);
	}
	
	/**
	 * Computes the Y-dimension for a given element.
	 */
	public float computeY(Node element) {
		return y.compute(element, false);
	}
	
	/**
	 * Checks whether this pair of dimensions computes to <code>0</code> for a given element.
	 */
	public boolean isZero(Node element) {
		return computeX(element) == 0 || computeY(element) == 0;
	}
	
	@Override
	public DualDimension clone() {
		return new DualDimension(x.clone(), y.clone());
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DualDimension dualDimension) ? equals(dualDimension) : super.equals(obj);
	}
	
	@Override
	public boolean equals(DualDimension other) {
		return other.x.equals(this.x) && other.y.equals(this.y);
	}
	
	public static DualDimension parse(String input) {
		String[] sections = input.trim().split("\\s+");
		ArrayList<DimensionBase> dimensions = new ArrayList<>();
		
		for (String section : sections) {
			DimensionBase dimension = Dimension.parse(section);
			
			if (dimension == null) {
				return null;
			}
			
			dimensions.add(dimension);
		}
		
		return switch (dimensions.size()) {
			case 1 -> new DualDimension(dimensions.get(0));
			case 2 -> new DualDimension(dimensions.get(0), dimensions.get(1));
			default -> null;
		};
	}
	
	/**
	 * Creates a pair of dimensions with all values set to <code>0</code>.
	 */
	public static DualDimension zero() {
		return new DualDimension(0);
	}
	
	/**
	 * Creates a pair of dimensions based on the viewport width and height.
	 */
	public static DualDimension fullscreen() {
		return new DualDimension(Dimension.viewportWidth(), Dimension.viewportHeight());
	}
	
	public static DualDimension parentSize() {
		return new DualDimension(Dimension.parentSize(), Dimension.parentSize());
	}
	
	public static DualDimension parentWidth() {
		return new DualDimension(Dimension.parentSize(), Dimension.auto());
	}
	
	public static DualDimension parentHeight() {
		return new DualDimension(Dimension.auto(), Dimension.parentSize());
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", x, y);
	}
}
