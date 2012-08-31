package com.cbroglie.eliminationscheduler.client;

import com.cbroglie.eliminationscheduler.client.event.CurrentUserChangedEvent;
import com.cbroglie.eliminationscheduler.shared.GetCurrentUserAction;
import com.cbroglie.eliminationscheduler.shared.GetCurrentUserResult;
import com.cbroglie.eliminationscheduler.shared.model.UserDetails;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasEventBus;

public class CurrentUser implements HasEventBus {

	private static final int REFRESH_DELAY = 5000; // ms
	private static final int RETRY_DELAY = 10000; // ms

	private final EventBus eventBus;
	private final DispatchAsync dispatcher;
	private final Timer fetchUserTimer;

	private UserDetails user;
	private boolean confirmed;

	@Inject
	public CurrentUser(final EventBus eventBus, final DispatchAsync dispatcher) {
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;

		this.fetchUserTimer = new Timer() {
			@Override
			public void run() {
				fetchUser();
			}
		};

		fetchUser();
	}

	/**
	 * Fetches the user information from the server.
	 */
	private void fetchUser() {
		String requestUri = GWT.getHostPageBaseURL();

		fetchUserTimer.cancel();
		dispatcher.execute(new GetCurrentUserAction(requestUri), new AsyncCallback<GetCurrentUserResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// Async call is back. We know if user is logged-in or not.
				confirmed = true;
				failed();
			}

			@Override
			public void onSuccess(GetCurrentUserResult result) {
				// Async call is back. We know if user is logged-in or not.
				confirmed = true;
				if (result != null) {
					user = result.getUser();
					scheduleFetchUser(REFRESH_DELAY);
					if (user.isLoggedIn()) {
						CurrentUserChangedEvent.fire(CurrentUser.this, CurrentUser.this);
					}
					else {
						CurrentUserChangedEvent.fire(CurrentUser.this, null);
					}
				} else {
					failed();
				}
			}

			private void failed() {
				// Nobody is logged in.
				user = null;
				scheduleFetchUser(RETRY_DELAY);
				CurrentUserChangedEvent.fire(CurrentUser.this, null); 
			}
		});
	}

	private void scheduleFetchUser(int delay) {
		fetchUserTimer.cancel();
		fetchUserTimer.schedule(delay);
	}

	/**
	 * Checks if the currently logged in user is an administrator.
	 * 
	 * @return <code>true</code> if the current user is an administrator. 
	 *				 <code>false</code> if he is not logged in or if he is not administrator.	
	 */
	public boolean isAdministrator() {
		if (isLoggedIn()) {
			return user.isAdministrator();
		}

		return false;
	}

	/**
	 * Check if a user is logged in. Will return false if the user is unconfirmed,
	 * see {@link #isConfirmed()}.
	 * 
	 * @return <code>true</code> if a user is logged in. <code>false</code> otherwise. 
	 */
	public boolean isLoggedIn() {
		if (user != null) {
			return user.isLoggedIn();
		}
		
		return false;
	}

	/**
	 * The user is unconfirmed when the application starts, before the first asynchronous
	 * call to the server has returned. As soon as this call as returned, we know if the
	 * user is logged in or not and this method will return <code>true</code>. At that
	 * point, call {@link #isLoggedIn()}.
	 * 
	 * @return <code>true</code> if the user is confirmed. <code>false</code> otherwise. 
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	public String getEmail() {
		if (user != null) {
			return user.getEmail();
		}

		return null;
	}

	public String getNickname() {
		if (user != null) {
			return user.getNickname();
		}

		return null;
	}

	public String getUserId() {
		if (user != null) {
			return user.getUserId();
		}

		return null;
	}

	public String getLoginUrl() {
		if (user != null) {
			if (!user.isLoggedIn()) {
				return user.getLoginUrl();
			}
		}
		
		return null;
	}

	public String getLogoutUrl() {
		if (isLoggedIn()) {
			return user.getLogoutUrl();
		}
		
		return null;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
}
