package com.cbroglie.eliminationscheduler.client.ui;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;

public interface HasSelectedValue<T> extends HasValue<T> {
	void setItems(Collection<T> items);

	void setSelectedItem(T selected);
	T getSelectedItem();
}
