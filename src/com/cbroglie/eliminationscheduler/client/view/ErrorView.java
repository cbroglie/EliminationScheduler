package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.ErrorPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ErrorView extends CompositeView implements ErrorPresenter.MyView {

	private static ErrorViewUiBinder uiBinder = GWT.create(ErrorViewUiBinder.class);

	interface ErrorViewUiBinder extends UiBinder<Widget, ErrorView> {}

	@UiField
	Label label;

	public ErrorView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setText(String text) {
		label.setText(text);
	}
}
