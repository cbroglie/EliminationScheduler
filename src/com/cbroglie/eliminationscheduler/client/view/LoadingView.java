package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.LoadingPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

public class LoadingView extends PopupViewImpl implements LoadingPresenter.MyView {

	private static LoadingViewUiBinder uiBinder = GWT.create(LoadingViewUiBinder.class);

	interface LoadingViewUiBinder extends UiBinder<PopupPanel, LoadingView> {}

	private final PopupPanel widget;

	@Inject
	public LoadingView(EventBus eventBus) {
		super(eventBus);
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
