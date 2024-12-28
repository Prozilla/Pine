package dev.prozilla.pine.common.math.dimension;

import dev.prozilla.pine.common.Printable;
import dev.prozilla.pine.core.component.canvas.RectTransform;

import java.util.ArrayList;

/**
 * Represents a pair of dimensions (X and Y) for a UI element. Each dimension is defined using a {@link DimensionBase}.
 * @see Dimension
 */
public class DualDimension implements Printable {
	
	public DimensionBase x;
	public DimensionBase y;
	
	public static final int DEFAULT_VALUE = 0;
	
	/**
	 * Creates a default pair of dimensions with all values set to 0 pixels.
	 */
	public DualDimension() {
		this(DEFAULT_VALUE);
	}
	
	public DualDimension(int xy) {
		this(new Dimension(xy));
	}
	
	/**
	 * Creates a pair of dimensions with two equal values.
	 */
	public DualDimension(DimensionBase xy) {
		this(xy, xy);
	}
	
	public DualDimension(int x, int y) {
		this(new Dimension(x), new Dimension(y));
	}
	
	public DualDimension(DimensionBase x, DimensionBase y) {
		this.x = x;
		this.y = y;
	}
	
	public void reset() {
		set(DEFAULT_VALUE);
	}
	
	public void set(int xy) {
		set(new Dimension(xy));
	}
	
	public void set(DimensionBase xy) {
		set(xy, xy);
	}
	
	public void set(int x, int y) {
		set(new Dimension(x), new Dimension(y));
	}
	
	public void set(DimensionBase x, DimensionBase y) {
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
	public int computeX(RectTransform element) {
		return x.compute(element);
	}
	
	/**
	 * Computes the Y-dimension for a given element.
	 */
	public int computeY(RectTransform element) {
		return y.compute(element);
	}
	
	/**
	 * Checks whether this pair of dimensions computes to <code>0</code> for a given element.
	 */
	public boolean isZero(RectTransform element) {
		return computeX(element) == 0 || computeY(element) == 0;
	}
	
	@Override
	public DualDimension clone() {
		return new DualDimension(x.clone(), y.clone());
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof DualDimension dualDimension
				&& dualDimension.x.equals(this.x) && dualDimension.y.equals(this.y);
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
	
	@Override
	public String toString() {
		return String.format("(%s,%s)", x, y);
	}
}
