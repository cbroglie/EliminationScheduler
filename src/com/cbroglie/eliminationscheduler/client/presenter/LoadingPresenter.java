package com.cbroglie.eliminationscheduler.client.presenter;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class LoadingPresenter extends PresenterWidget<LoadingPresenter.MyView>{

	public interface MyView extends PopupView {}

	@Inject
	public LoadingPresenter(EventBus eventBus, MyView view) {
		super(eventBus, view);
	}
}
