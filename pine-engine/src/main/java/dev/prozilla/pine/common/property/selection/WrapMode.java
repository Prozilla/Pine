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
	 * <table>
	 *     <thead>
	 *         <th colspan="8">Example: {@code String[] array = new String[]{"A", "B", "C"}}</th>
	 *     </thead>
	 *     <tbody>
	 *          <tr>
	 *              <th scope="row">{@code i}</th>
	 *              <td>-2</td>
	 *              <td>-1</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>3</td>
	 *              <td>4</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code list[i]}</th>
	 *              <td></td>
	 *              <td></td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td></td>
	 *              <td></td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code transformIndex(i, list)}</th>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code getElement(i, list)}</th>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *          </tr>
	 *     </tbody>
	 * </table>
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
		
		/**
		 * Changes the size of a list by either removing or repeating elements.
		 *
		 * <p>If the target size is greater than the size of the list, each element is repeated until the list has the target size.</p>
		 *
		 * <p>For example, if the target size is twice the original size of the list, the entire list will essentially be duplicated. The frequency of each element will be doubled.</p>
		 */
		@Override
		public <E> void resizeList(List<E> list, int targetSize) {
			super.resizeList(list, targetSize);
		}
	},
	
	/**
	 * Values outside the bounds are considered invalid and return {@code -1} to indicate no valid result.
	 *
	 * <table>
	 *     <thead>
	 *         <th colspan="8">Example: {@code String[] array = new String[]{"A", "B", "C"}}</th>
	 *     </thead>
	 *     <tbody>
	 *          <tr>
	 *              <th scope="row">{@code i}</th>
	 *              <td>-2</td>
	 *              <td>-1</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>3</td>
	 *              <td>4</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code list[i]}</th>
	 *              <td></td>
	 *              <td></td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td></td>
	 *              <td></td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code transformIndex(i, list)}</th>
	 *              <td>-1</td>
	 *              <td>-1</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>-1</td>
	 *              <td>-1</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code getElement(i, list)}</th>
	 *              <td>null</td>
	 *              <td>null</td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td>null</td>
	 *              <td>null</td>
	 *          </tr>
	 *     </tbody>
	 * </table>
	 */
	CLIP {
		/**
		 * @return {@code value} if it is within the given bounds, otherwise {@code -1}.
		 */
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			
			if (value < min || value > max) {
				return -1;
			}
			
			return value;
		}
		
		/**
		 * Changes the size of a list by either removing elements or adding {@code null}.
		 *
		 * <p>If the target size is greater than the size of the list, {@code null} is added until the list has the target size.</p>
		 */
		@Override
		public <E> void resizeList(List<E> list, int targetSize) {
			super.resizeList(list, targetSize);
		}
	},
	
	/**
	 * Values outside the bounds are forced to the nearest valid bound.
	 *
	 * <table>
	 *     <thead>
	 *         <th colspan="8">Example: {@code String[] array = new String[]{"A", "B", "C"}}</th>
	 *     </thead>
	 *     <tbody>
	 *          <tr>
	 *              <th scope="row">{@code i}</th>
	 *              <td>-2</td>
	 *              <td>-1</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>3</td>
	 *              <td>4</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code list[i]}</th>
	 *              <td></td>
	 *              <td></td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td></td>
	 *              <td></td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code transformIndex(i, list)}</th>
	 *              <td>0</td>
	 *              <td>0</td>
	 *              <td>0</td>
	 *              <td>1</td>
	 *              <td>2</td>
	 *              <td>2</td>
	 *              <td>2</td>
	 *          </tr>
	 *          <tr>
	 *              <th scope="row">{@code getElement(i, list)}</th>
	 *              <td>A</td>
	 *              <td>A</td>
	 *              <td>A</td>
	 *              <td>B</td>
	 *              <td>C</td>
	 *              <td>C</td>
	 *              <td>C</td>
	 *          </tr>
	 *     </tbody>
	 * </table>
	 */
	CLAMP {
		@Override
		public int transform(int value, int min, int max) {
			checkBounds(min, max);
			return MathUtils.clamp(value, min, max);
		}
		
		/**
		 * Changes the size of a list by either removing or adding the last element in the list repeatedly.
		 *
		 * <p>If the target size is greater than the size of the list, the last element is added until the list has the target size.</p>
		 */
		@Override
		public <E> void resizeList(List<E> list, int targetSize) {
			super.resizeList(list, targetSize);
		}
	};
	
	/**
	 * Changes the size of a list by either removing elements or adding elements based on this wrap mode.
	 *
	 * <p>If the target size is less than the size of the list, the last element is removed until the list has the target size.</p>
	 * <p>If the target size is greater than the size of the list, the next element according to this wrap mode is added until the list has the target size.</p>
	 * @param list The list to resize
	 * @param targetSize The target size
	 * @param <E> The type of elements in the list
	 */
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
	 * Returns the element of the given list that succeeds the given element.
	 * @return The successor of the given element.
	 * @param <E> The type of elements in the list
	 */
	public <E> E getSuccessor(E element, List<E> list) {
		return getElement(list.indexOf(element) + 1, list);
	}
	
	/**
	 * Returns the element of the given list that precedes the given element.
	 * @return The predecessor of the given element.
	 * @param <E> The type of elements in the list
	 */
	public <E> E getPredecessor(E element, List<E> list) {
		return getElement(Math.min(list.indexOf(element) - 1, -1), list);
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
	 *
	 * <p>If it is within the bounds, the original value will be returned.</p>
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
