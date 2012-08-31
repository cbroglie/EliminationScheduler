package com.cbroglie.eliminationscheduler.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;

@Singleton
public class AdminGatekeeper implements Gatekeeper {

	private final CurrentUser currentUser;

	@Inject
	public AdminGatekeeper(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public boolean canReveal() {
		return currentUser.isAdministrator();
	}
}
