package dev.prozilla.pine.common.property.selection;

import dev.prozilla.pine.common.math.MathUtils;

import java.util.Collection;
import java.util.List;

/**
 * Determines how values outside of bounds are transformed.
 *
 * <p>Can be used to transform any given number into an index for an item in an array.</p>
 */
public enum WrapMode {
	REPEAT {
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			
			if (min == max) {
				return min;
			}
			
			int range = max - min + 1;
			int shifted = value - min;
			int wrapped = ((shifted % range) + range) % range;
			
			return wrapped + min;
		}
	},
	CLIP {
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			
			if (value < min || value > max) {
				return -1;
			}
			
			return value;
		}
	},
	CLAMP {
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			return MathUtils.clamp(value, min, max);
		}
	};
	
	public <E> E getElement(int index, List<E> list) {
		return list.get(transformIndex(index, list));
	}
	
	public <E> E getElement(int index, E[] array) {
		return array[transformIndex(index, array)];
	}
	
	/**
	 * Transforms an index based on the size of a collection.
	 * @param index The index to transform
	 * @param collection The collection
	 * @return The transformed index. ({@code -1} represents an empty value.)
	 */
	public int transformIndex(int index, Collection<?> collection) {
		return transform(index, 0, collection.size() - 1);
	}
	
	/**
	 * Transforms an index based on the size of an array.
	 * @param index The index to transform
	 * @param array The array
	 * @return The transformed index. ({@code -1} represents an empty value.)
	 */
	public <E> int transformIndex(int index, E[] array) {
		return transform(index, 0, array.length - 1);
	}
	
	/**
	 * Transforms a value based on the given bounds.
	 * @param value The value to wrap
	 * @param min The lower bound
	 * @param max The upper bound
	 * @return The transformed value. ({@code -1} represents an empty value.)
	 * @throws IllegalArgumentException If the bounds are invalid.
	 */
	public abstract int transform(int value, int min, int max) throws IllegalArgumentException;
	
	/**
	 * Checks if the given bounds are valid, and throws an exception if they're not.
	 * @param min The lower bound
	 * @param max The upper bound
	 * @throws IllegalArgumentException If the bounds are invalid.
	 */
	private static void checkBounds(int min, int max) throws IllegalArgumentException {
		if (min > max) {
			throw new IllegalArgumentException("Lower bound must not exceed upper bound");
		}
		if (min < 0) {
			throw new IllegalArgumentException("Lower bound must be positive");
		}
	}
	
}
