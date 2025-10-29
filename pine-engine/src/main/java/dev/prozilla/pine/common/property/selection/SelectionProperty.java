package dev.prozilla.pine.common.property.selection;

import dev.prozilla.pine.common.property.observable.SimpleObservableObjectProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.Collections;
import java.util.List;

/**
 * Represents a selection of items from a list.
 * @param <I> The type of selectable items.
 * @param <S> The type of the selection.
 */
public abstract class SelectionProperty<I, S> extends SimpleObservableObjectProperty<S> {
	
	protected final List<I> items;
	protected WrapMode wrapMode;
	
	public static final WrapMode DEFAULT_WRAP_MODE = WrapMode.REPEAT;
	
	public SelectionProperty(List<I> items) {
		super(null);
		this.items = Checks.isNotNull(items, "items");
		wrapMode = DEFAULT_WRAP_MODE;
	}
	
	/**
	 * Returns an unmodifiable clone of the list of items.
	 * @return A clone of the list of items.
	 */
	public List<I> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	public boolean addItem(I item) {
		return items.add(item);
	}
	
	public boolean removeItem(I item) {
		return items.remove(item);
	}
	
	/**
	 * Selects an item from the list of items.
	 * @param item The item to select
	 * @return {@code false} if the item was already selected.
	 * @throws IllegalArgumentException If the item is not in the list of items.
	 */
	public boolean selectItem(I item) throws IllegalArgumentException {
		if (!items.contains(item)) {
			throw new IllegalArgumentException("Unknown item: " + item);
		}
		
		return selectIndex(items.indexOf(item));
	}
	
	/**
	 * Selects an item from the list of items at a given index.
	 * @param index The index of the item to select
	 * @return {@code false} if the item was already selected.
	 */
	public abstract boolean selectIndex(int index);
	
	/**
	 * Selects the first item.
	 */
	public void selectFirst() {
		selectIndex(0);
	}
	
	/**
	 * Selects the last item.
	 */
	public void selectLast() {
		selectIndex(items.size() - 1);
	}
	
	/**
	 * Selects the previous item.
	 */
	public abstract void selectPrevious();
	
	/**
	 * Selects the next item.
	 */
	public abstract void selectNext();
	
	/**
	 * Returns the item at the given index based on the wrap mode.
	 * @param index The index of the item
	 * @return The item at the given index or {@code null} if there is no corresponding item.
	 */
	public I getItem(int index) {
		index = transformIndex(index);
		
		if (index < 0) {
			return null;
		}
		
		return items.get(index);
	}
	
	/**
	 * Transforms an index based on the wrap mode.
	 * @param index The index to transform
	 * @return The transformed index.
	 */
	public int transformIndex(int index) {
		return wrapMode.transformIndex(index, items);
	}
	
	public void setWrapMode(WrapMode wrapMode) {
		this.wrapMode = wrapMode;
	}
	
}
