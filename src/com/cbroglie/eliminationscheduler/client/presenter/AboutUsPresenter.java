package com.cbroglie.eliminationscheduler.client.presenter;

import com.cbroglie.eliminationscheduler.client.NameTokens;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class AboutUsPresenter extends Presenter<AboutUsPresenter.MyView, AboutUsPresenter.MyProxy> {

	public interface MyView extends View {
		void setVersionText(String text);
		void setAuthorText(String text);
		void setContactText(String text);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.aboutUsPage)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<AboutUsPresenter> {}

	@Inject
	public AboutUsPresenter(EventBus eventBus, MyView view, MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setVersionText("EliminationScheduler version 0.1");
		getView().setAuthorText("Created by Chris Broglie");
		getView().setContactText("Please send any issues to cbroglie@gmail.com");
	}
}
