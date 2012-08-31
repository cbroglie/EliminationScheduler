package com.cbroglie.eliminationscheduler.client.presenter;

import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.event.CurrentUserChangedEvent;
import com.cbroglie.eliminationscheduler.client.event.CurrentUserChangedEvent.CurrentUserChangedHandler;
import com.cbroglie.eliminationscheduler.client.view.LoginUiHandlers;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;

public class LoginPresenter extends PresenterWidget<LoginPresenter.MyView> implements LoginUiHandlers, CurrentUserChangedHandler {

	public interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
		void setLoggedIn(String username, boolean isAdministrator, String logoutUrl);
		void setLoggedOut(String loginUrl);
	}

	private final CurrentUser currentUser;

	@Inject
	public LoginPresenter(
			final EventBus eventBus,
			final MyView view,
			final CurrentUser currentUser) {
		super(eventBus, view);

		this.currentUser = currentUser;

		getView().setUiHandlers(this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		checkUserStatus();
	}

	@Override
	public void onLogout() {
		CurrentUserChangedEvent.fire(this, null);
	}

	@ProxyEvent
	@Override
	public void onCurrentUserChanged(CurrentUserChangedEvent event) {
		checkUserStatus();
	}

	private void checkUserStatus() {
		if (currentUser.isLoggedIn()) {
			getView().setLoggedIn(currentUser.getEmail(), currentUser.isAdministrator(), currentUser.getLogoutUrl());
		}
		else {
			getView().setLoggedOut(currentUser.getLoginUrl());
		}
	}
}
