package dev.prozilla.pine.common.property.selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a selection of zero or one item(s) from a list.
 */
public class SingleSelectionProperty<T> extends SelectionProperty<T, T> {
	
	private int selectedIndex;
	
	public SingleSelectionProperty() {
		this(new ArrayList<>());
	}
	
	@SafeVarargs
	public SingleSelectionProperty(T... items) {
		this(Arrays.stream(items).toList());
	}
	
	public SingleSelectionProperty(List<T> items) {
		super(items);
		selectedIndex = -1;
	}
	
	@Override
	public boolean removeItem(T item) {
		if (isItemSelected(item)) {
			clearSelection();
		}
		return super.removeItem(item);
	}
	
	@Override
	public boolean setValue(T value) {
		return selectItem(value);
	}
	
	@Override
	public boolean selectItem(T item) throws IllegalArgumentException {
		if (item == null) {
			return clearSelection();
		}
		
		return super.selectItem(item);
	}
	
	@Override
	public void selectPrevious() {
		if (wrapMode == WrapMode.CLIP && selectedIndex >= items.size()) {
			return;
		}
		selectIndex(selectedIndex - 1);
	}
	
	@Override
	public void selectNext() {
		if (wrapMode == WrapMode.CLIP && selectedIndex < 0) {
			return;
		}
		selectIndex(selectedIndex + 1);
	}
	
	@Override
	public boolean selectIndex(int index) {
		if (items.isEmpty()) {
			return clearSelection();
		}
		
		index = transformIndex(index);
		
		if (selectedIndex == index) {
			return false;
		}
		
		selectedIndex = index;
		return super.setValue(getItem(index));
	}
	
	public boolean clearSelection() {
		if (selectedIndex == -1) {
			return false;
		}
		
		selectedIndex = -1;
		return super.setValue(null);
	}
	
	public T getSelectedItem() {
		return getValue();
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public boolean isItemSelected(T item) {
		T value = getValue();
		return value != null && value.equals(item);
	}
	
	public boolean isAnyItemSelected() {
		return getValue() != null;
	}
	
}
