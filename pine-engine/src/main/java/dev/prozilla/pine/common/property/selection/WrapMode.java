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
	/**
	 * Values outside the bounds wrap around and re-enter from the opposite side, creating a continuous loop.
	 *
	 * <p>Example: With bounds 0–4, an input of 5 becomes 0, and an input of -1 becomes 4.</p>
	 */
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
	
	/**
	 * Values outside the bounds are considered invalid and return {@code -1} to indicate no valid result.
	 *
	 * <p>Example: With bounds 0–4, inputs like -2 or 6 will return -1.</p>
	 */
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
	
	/**
	 * Values outside the bounds are forced to the nearest valid bound.
	 *
	 * <p>Example: With bounds 0–4, an input of 6 becomes 4, and an input of -2 becomes 0.</p>
	 */
	CLAMP {
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			return MathUtils.clamp(value, min, max);
		}
	};
	
	public <E> void resizeList(List<E> list, int targetSize) {
		int originalSize = list.size();
		if (targetSize < originalSize) {
			for (int i = 0; i < (originalSize - targetSize); i++) {
				list.removeLast();
			}
		} else if (targetSize > originalSize) {
			for (int i = originalSize; i < targetSize; i++) {
				int transformedIndex = transform(i, 0, originalSize - 1);
				if (transformedIndex >= 0) {
					list.add(list.get(transformedIndex));
				} else {
					list.add(null);
				}
			}
		}
	}
	
	/**
	 * Applies this wrap mode to an index for a list and returns the corresponding element.
	 * <p>The transformation is based on the list's index range ({@code 0} to {@code list.size() - 1}).</p>
	 *
	 * @param index The raw index value to transform
	 * @param list The list from which to retrieve the element
	 * @return The element at the transformed index, or {@code null} if the transformed index represents an empty value.
	 * @param <E> The type of elements in the list
	 * @see #transformIndex(int, Collection)
	 */
	public <E> E getElement(int index, List<E> list) {
		index = transformIndex(index, list);
		if (index < 0) {
			return null;
		}
		return list.get(index);
	}
	
	/**
	 * Applies this wrap mode to an index for an array and returns the corresponding element.
	 * <p>The transformation is based on the array's index range ({@code 0} to {@code array.length - 1}).</p>
	 *
	 * @param index The raw index value to transform
	 * @param array The array from which to retrieve the element
	 * @return The element at the transformed index, or {@code null} if the transformed index represents an empty value.
	 * @param <E> The type of elements in the array
	 * @see #transformIndex(int, Object[]) 
	 */
	public <E> E getElement(int index, E[] array) {
		index = transformIndex(index, array);
		if (index < 0) {
			return null;
		}
		return array[index];
	}
	
	/**
	 * Transforms an index based on the index range of a list ({@code 0} to {@code list.size() - 1}).
	 * @param index The index to transform
	 * @param collection The collection
	 * @return The transformed index. ({@code -1} represents an empty value.)
	 */
	public int transformIndex(int index, Collection<?> collection) {
		return transform(index, 0, collection.size() - 1);
	}
	
	/**
	 * Transforms an index based on the index range of an array ({@code 0} to {@code array.length - 1}).
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
