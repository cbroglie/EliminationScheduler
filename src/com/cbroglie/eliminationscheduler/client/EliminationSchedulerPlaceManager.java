package com.cbroglie.eliminationscheduler.client;

import com.cbroglie.eliminationscheduler.client.gin.DefaultPlace;
import com.cbroglie.eliminationscheduler.client.gin.ErrorPlace;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class EliminationSchedulerPlaceManager extends PlaceManagerImpl {

	private static final int RETRY_DELAY = 500; // ms
	private static final int MAX_RETRIES = 4;

	private final CurrentUser currentUser;
	private final Timer retryTimer;
	private final PlaceRequest defaultPlaceRequest;
	private final PlaceRequest errorPlaceRequest;

	private int retryCount = 0;

	@Inject
	public EliminationSchedulerPlaceManager(final EventBus eventBus,
			final TokenFormatter tokenFormatter,
			final CurrentUser currentUser,
			@DefaultPlace String defaultNameToken,
			@ErrorPlace String errorNameToken) {
		super(eventBus, tokenFormatter);

		this.currentUser = currentUser;
		this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
		this.errorPlaceRequest = new PlaceRequest(errorNameToken);

		this.retryTimer = new Timer() {
			@Override
			public void run() {
				History.fireCurrentHistoryState();
			}
		};
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(defaultPlaceRequest);
	}

	@Override
	public void revealErrorPlace(String invalidHistoryToken) {
		if (!currentUser.isConfirmed() && (retryCount < MAX_RETRIES)) {
			retryTimer.schedule(RETRY_DELAY);
		}
		else {
			revealPlace(errorPlaceRequest);
		}
	}
}
