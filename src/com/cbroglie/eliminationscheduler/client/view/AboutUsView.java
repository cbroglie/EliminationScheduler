package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.AboutUsPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AboutUsView extends CompositeView implements AboutUsPresenter.MyView {

	private static AboutUsViewUiBinder uiBinder = GWT.create(AboutUsViewUiBinder.class);

	interface AboutUsViewUiBinder extends UiBinder<Widget, AboutUsView> {}

	@UiField
	Label versionLabel;

	@UiField
	Label authorLabel;

	@UiField
	Label contactLabel;

	public AboutUsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setVersionText(String text) {
		versionLabel.setText(text);
	}

	@Override
	public void setAuthorText(String text) {
		authorLabel.setText(text);
	}

	@Override
	public void setContactText(String text) {
		contactLabel.setText(text);
	}
}
