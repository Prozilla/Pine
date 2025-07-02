package dev.prozilla.pine.common.property.selection;

import dev.prozilla.pine.common.property.observable.ObservableProperty;
import dev.prozilla.pine.common.util.checks.Checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectionProperty<T> extends ObservableProperty<T> {
	
	private final List<T> items;
	private int selectedIndex;
	
	public SelectionProperty() {
		this(new ArrayList<>());
	}
	
	@SafeVarargs
	public SelectionProperty(T... items) {
		this(Arrays.stream(items).toList());
	}
	
	public SelectionProperty(List<T> items) {
		super(null);
		this.items = Checks.isNotNull(items, "items");
		selectedIndex = -1;
	}
	
	public List<T> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public int getItemCount() {
		return items.size();
	}
	
	public boolean addItem(T item) {
		return items.add(item);
	}
	
	public boolean removeItem(T item) {
		if (isItemSelected(item)) {
			clearSelection();
		}
		return items.remove(item);
	}
	
	@Override
	public boolean setValue(T value) {
		return setSelectedItem(value);
	}
	
	public boolean setSelectedItem(T item) {
		if (item == null) {
			return clearSelection();
		}
		
		if (!items.contains(item)) {
			throw new IllegalArgumentException("Unknown item: " + item);
		}
		
		return setSelectedIndex(items.indexOf(item));
	}
	
	public void selectFirst() {
		setSelectedIndex(0);
	}
	
	public void selectLast() {
		setSelectedIndex(-1);
	}
	
	public void selectPrevious() {
		setSelectedIndex(selectedIndex - 1);
	}
	
	public void selectNext() {
		setSelectedIndex(selectedIndex + 1);
	}
	
	public boolean setSelectedIndex(int index) {
		if (items.isEmpty()) {
			return clearSelection();
		}
		
		while (index < 0) {
			index += items.size();
		}
		
		while (index >= items.size()) {
			index -= items.size();
		}
		
		if (selectedIndex == index) {
			return false;
		}
		
		selectedIndex = index;
		return super.setValue(items.get(index));
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
