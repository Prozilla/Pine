package dev.prozilla.pine.common;

/**
 * Represents an abstract container with items.
 * @param <T> Type of the items in this container
 */
public interface Container<T> extends Iterable<T> {
	
	/**
	 * Adds an item to this container.
	 * @param item Item to add to this container
	 * @return True if the container changed after adding the item
	 */
	boolean add(T item);
	
	/**
	 * Removes all items from this container.
	 */
	void clear();
	
	/**
	 * @return True if this container has no items.
	 */
	boolean isEmpty();
	
	/**
	 * @return The amount of items in this container.
	 */
	int size();

}
