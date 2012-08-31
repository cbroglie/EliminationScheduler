package com.cbroglie.eliminationscheduler.client.ui;

import java.util.Collection;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ListBox;

public class SelectOneListBox<T> extends ListBox implements HasSelectedValue<T> {

	public interface ItemFormatter<T> {
		abstract String getLabel(T item);
		abstract String getValue(T item);
	}

	private final ItemFormatter<T> formatter;

	private boolean valueChangeHandlerInitialized = false;
	private T[] items = null;

	public SelectOneListBox(ItemFormatter<T> formatter) {
		this.formatter = formatter;
	}

	@Override
	public T getValue() {
		return getSelectedItem();
	}

	@Override
	public void setValue(T value) {
		setValue(value, false);
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		T oldValue = getValue();

		setSelectedItem(value);

		if (fireEvents) {
			ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
		if (!valueChangeHandlerInitialized) {
			valueChangeHandlerInitialized = true;

			super.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					ValueChangeEvent.fire(SelectOneListBox.this, getValue());
				}
			});
		}

		return addHandler(handler, ValueChangeEvent.getType());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setItems(Collection<T> items) {
		// Clear all existing items.
		super.clear();

		// Add the new items to the list.
		this.items = (T[])items.toArray();
		for (T item : items) {
			String label = formatter.getLabel(item);
			String value = formatter.getValue(item);
			super.addItem(label, value);
		}
	}

	@Override
	public void setSelectedItem(T item) {
		int numItems = super.getItemCount();
		for (int itemIdx = 0; itemIdx < numItems; itemIdx++) {
			String label = super.getItemText(itemIdx);
			if (formatter.getLabel(item).equals(label)) {
				super.setSelectedIndex(itemIdx);
				return;
			}
		}

		throw new IllegalArgumentException("No index found for item " + item.toString());
	}

	@Override
	public T getSelectedItem() {
		int selectedIndex = super.getSelectedIndex(); 
		if (selectedIndex >= 0) {
			String name = super.getValue(selectedIndex);
			for (T item : items) {
				if (formatter.getValue(item).equals(name)) {
					return item;
				}
			}
		}

		return null;
	}
}
