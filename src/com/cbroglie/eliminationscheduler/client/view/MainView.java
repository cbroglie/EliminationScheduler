package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.MainPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainView extends CompositeView implements MainPresenter.MyView {

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {}

	@UiField
	FlowPanel mainContentPanel;

	@UiField
	FlowPanel loginPanel;

	@Inject
	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPresenter.TYPE_SetLoginContent) {
			setLoginContent(content);
		}
		else if (slot == MainPresenter.TYPE_SetMainContent) {
			setMainContent(content);
		}
		else {
			super.setInSlot(slot, content);
		}
	}

	private void setLoginContent(Widget content) {
		loginPanel.clear();
		if (content != null) {
			loginPanel.add(content);
		}
	}

	private void setMainContent(Widget content) {
		mainContentPanel.clear();
		if (content != null) {
			mainContentPanel.add(content);
		}
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
