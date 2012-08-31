package com.cbroglie.eliminationscheduler.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.View;

/**
 * A simple implementation of {@link View} that simply disregards every call to
 * {@link #setInSlot(Object, Widget)}, {@link #addToSlot(Object, Widget)}, and
 * {@link #clearSlot(Object)}.
 * <p />
 * Views can extend this rather than {@link com.google.gwt.user.client.ui.Composite})
 * for use with UiBinder.
 *
 * @author cbroglie
 *
 */
public abstract class CompositeView extends Composite implements View {

	@Override
	public void addContent(Object slot, Widget content) {
		addToSlot(slot, content);
	}

	@Override
	public void addToSlot(Object slot, Widget content) {
	}

	@Override
	public void removeContent(Object slot, Widget content) {
		removeFromSlot(slot, content);
	}

	@Override
	public void removeFromSlot(Object slot, Widget content) {
	}

	@Override
	public void setContent(Object slot, Widget content) {
		setInSlot(slot, content);
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
	}
}
