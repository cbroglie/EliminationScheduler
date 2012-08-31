package com.cbroglie.eliminationscheduler.client.presenter;

import com.cbroglie.eliminationscheduler.client.event.LoadingEvent;
import com.cbroglie.eliminationscheduler.client.event.LoadingEvent.LoadingHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

public class MainPresenter extends Presenter<MainPresenter.MyView, MainPresenter.MyProxy> implements LoadingHandler {

	public interface MyView extends View {}

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPresenter> {}

	/**
	 * Use this in leaf presenters, inside their {@link #revealInParent} method.
	 */
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	public static final Object TYPE_SetLoginContent = new Object();

	private final LoginPresenter loginPresenter;
	private final LoadingPresenter loadingPresenter;

	@Inject
	public MainPresenter(
			final EventBus eventBus,
			final MyView view,
			final MyProxy proxy,
			final LoginPresenter loginPresenter,
			final LoadingPresenter loadingPresenter) {
		super(eventBus, view, proxy);
		this.loginPresenter = loginPresenter;
		this.loadingPresenter = loadingPresenter;
	}

	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(LoadingEvent.getType(), this);
	}

	@Override
	protected void revealInParent() {
		// Hide the loading progress bar.
		DOM.setStyleAttribute(RootPanel.get("loading").getElement(), "display", "none");

		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_SetLoginContent, loginPresenter);
	}

	@Override
	protected void onHide() {
		super.onHide();
		clearSlot(TYPE_SetLoginContent);
	}

	@Override
	public void onLoading(LoadingEvent event) {
		if (event.isFinished()) {
			loadingPresenter.getView().hide();	
		}
		else {
			addToPopupSlot(loadingPresenter);
		}
	}
}
