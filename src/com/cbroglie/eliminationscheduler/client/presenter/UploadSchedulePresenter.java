package com.cbroglie.eliminationscheduler.client.presenter;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;

import com.cbroglie.eliminationscheduler.client.NameTokens;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class UploadSchedulePresenter extends Presenter<UploadSchedulePresenter.MyView, UploadSchedulePresenter.MyProxy> {

	public interface MyView extends View {
		IUploader getUploader();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.uploadSchedulePage)
	public interface MyProxy extends ProxyPlace<UploadSchedulePresenter> {}

	@Inject
	public UploadSchedulePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

		// Add handler for when the upload completes.
		getView().getUploader().addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				if (uploader.getStatus() == Status.SUCCESS) {
					System.out.println("Upload succeeeded");
				}
			}
		});
	}
}
