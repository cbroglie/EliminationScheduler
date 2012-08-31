package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.UploadSchedulePresenter;

import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class UploadScheduleView extends CompositeView implements UploadSchedulePresenter.MyView {

	private static UploadScheduleViewUiBinder uiBinder = GWT.create(UploadScheduleViewUiBinder.class);

	interface UploadScheduleViewUiBinder extends UiBinder<Widget, UploadScheduleView> {}

	@UiField
	SingleUploader uploader;

	public UploadScheduleView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public IUploader getUploader() {
		return uploader;
	}
}
