package com.cbroglie.eliminationscheduler.client.presenter;

import com.cbroglie.eliminationscheduler.client.CurrentUser;
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

public class ErrorPresenter extends Presenter<ErrorPresenter.MyView, ErrorPresenter.MyProxy> {

	public interface MyView extends View {
		void setText(String text);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.errorPage)
	@NoGatekeeper
	public interface MyProxy extends ProxyPlace<ErrorPresenter> {}

	private static final String PAGE_NOT_FOUND_MSG = "The requested page does not exist.";
	private static final String NOT_LOGGED_IN_MSG = "Please sign into your Google account to access this page.";
	private static final String NOT_CONFIRMED_MSG = "Please sign into your Google account to access this page. Refresh the page if you are already signed in.";

	final CurrentUser currentUser;

	@Inject
	public ErrorPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final CurrentUser currentUser) {
		super(eventBus, view, proxy);
		this.currentUser = currentUser;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		String text; 
		if (currentUser.isLoggedIn()) {
			text = PAGE_NOT_FOUND_MSG;
		}
		else if (!currentUser.isConfirmed()) {
			text = NOT_CONFIRMED_MSG;
		}
		else {
			text = NOT_LOGGED_IN_MSG;
		}

		getView().setText(text);
	}
}
