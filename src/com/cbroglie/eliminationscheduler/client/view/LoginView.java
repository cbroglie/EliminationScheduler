package com.cbroglie.eliminationscheduler.client.view;

import com.cbroglie.eliminationscheduler.client.presenter.LoginPresenter;
import com.cbroglie.eliminationscheduler.client.resources.Resources;
import com.cbroglie.eliminationscheduler.client.ui.TokenSeparatedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginView extends CompositeViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {

	private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

	interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {}

	@UiField(provided = true)
	final Resources resources;

	@UiField
	TokenSeparatedList bar;

	@UiField
	InlineLabel notSignedIn;

	@UiField(provided = true)
	final Anchor signIn;

	@UiField
	InlineLabel username;

	@UiField
	InlineHyperlink administration;

	@UiField(provided = true)
	final Anchor signOut;

	private boolean isLoggedIn;
	private boolean isAdministrator;

	@Inject
	public LoginView(Resources resources) {
		this.resources = resources;
		this.signIn = new Anchor("Sign in");
		this.signOut = new Anchor("Sign out");

		initWidget(uiBinder.createAndBindUi(this));
	}

	private void updateLoggedInVisibility() {
		bar.setWidgetVisible(notSignedIn, !isLoggedIn);
		bar.setWidgetVisible(signIn, !isLoggedIn);
		bar.setWidgetVisible(username, isLoggedIn);
		bar.setWidgetVisible(administration, isLoggedIn && isAdministrator);
		bar.setWidgetVisible(signOut, isLoggedIn);
	}

	@Override
	public void setLoggedIn(String username, boolean isAdministrator, String logoutUrl) {
		this.username.setText(username);
		this.signOut.setHref(logoutUrl);
		this.isLoggedIn = true;
		this.isAdministrator = isAdministrator;

		updateLoggedInVisibility();
	}

	@Override
	public void setLoggedOut(String loginUrl) {
		this.username.setText("");
		this.signIn.setHref(loginUrl);
		this.isLoggedIn = false;
		this.isAdministrator = false;

		updateLoggedInVisibility();
	}

	@UiHandler("signOut")
	void onSignOutClicked(ClickEvent event) {
		if (getUiHandlers() != null) {
			getUiHandlers().onLogout();
		}
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
