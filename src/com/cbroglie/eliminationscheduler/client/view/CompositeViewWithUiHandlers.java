package com.cbroglie.eliminationscheduler.client.view;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.UiHandlers;
import com.gwtplatform.mvp.client.View;

public abstract class CompositeViewWithUiHandlers<C extends UiHandlers> extends CompositeView implements HasUiHandlers<C> {

	private C uiHandlers;

	/**
	 * Access the {@link UiHandlers} associated with this {@link View}.
	 * <p>
	 * <b>Important!</b> Never call {@link #getUiHandlers()} inside your constructor
	 * since the {@link UiHandlers} are not yet set.
	 *  
	 *  @return The {@link UiHandlers}, or {@code null} if they are not yet set.
	 **/
	protected C getUiHandlers() {
		return uiHandlers;
	}

	@Override
	public void setUiHandlers(C uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
}
