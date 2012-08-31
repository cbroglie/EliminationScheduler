package com.cbroglie.eliminationscheduler.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.LoginGatekeeper;
import com.cbroglie.eliminationscheduler.client.NameTokens;
import com.cbroglie.eliminationscheduler.client.event.LoadingEvent;
import com.cbroglie.eliminationscheduler.client.view.MyPicksUiHandlers;
import com.cbroglie.eliminationscheduler.shared.GetEntriesAction;
import com.cbroglie.eliminationscheduler.shared.GetEntriesResult;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsAction;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsResult;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class MyPicksPresenter extends Presenter<MyPicksPresenter.MyView, MyPicksPresenter.MyProxy> implements MyPicksUiHandlers {

	public interface MyView extends View, HasUiHandlers<MyPicksUiHandlers> {
		void refreshEntries();
		void refreshSelections();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.myPicksPage)
	@UseGatekeeper(LoginGatekeeper.class)
	public interface MyProxy extends ProxyPlace<MyPicksPresenter> {}

	private final DispatchAsync dispatcher;
	private final CurrentUser currentUser;

	private ArrayList<EntryDetails> entries = null;
	private List<TeamDetails> selections = null;
	private EntryDetails activeEntry = null;

	private boolean entriesLoading = false;
	private boolean selectionsLoading = false;

	@Inject
	public MyPicksPresenter(
			final EventBus eventBus,
			final MyView view,
			final MyProxy proxy,
			final DispatchAsync dispatcher,
			final CurrentUser currentUser) {

		super(eventBus, view, proxy);

		this.dispatcher = dispatcher;
		this.currentUser = currentUser;

		getView().setUiHandlers(this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Clear all cached data that could have been modified externally.
		activeEntry = null;
		selections = null;
		entries = null;

		// Clear any stale values from the view.
		getView().refreshEntries();
		getView().refreshSelections();

		updateEntries();
	}

	private void updateEntries() {
		updateLoading(true, selectionsLoading);

		dispatcher.execute(new GetEntriesAction(currentUser.getUserId()), new AsyncCallback<GetEntriesResult>() {
			@Override
			public void onFailure(Throwable error) {
				updateLoading(false, selectionsLoading);
				handleError(error);
			}

			@Override
			public void onSuccess(GetEntriesResult result) {
				entries = result.getEntries();

				getView().refreshEntries();
				updateLoading(false, selectionsLoading);
			}
		});
	}

	private void handleError(Throwable error) {
		error.printStackTrace();
		if (error.getMessage() != null) {
			Window.alert(error.getMessage());
		}
		else {
			Window.alert("Internal error");
		}
	}

	private void updateLoading(boolean entriesLoading, boolean selectionsLoading) {
		boolean wasLoading = (this.entriesLoading || this.selectionsLoading);
		boolean isLoading = (entriesLoading || selectionsLoading);

		if (isLoading != wasLoading) {
			LoadingEvent.fire(this, !isLoading);
		}

		this.entriesLoading = entriesLoading;
		this.selectionsLoading = selectionsLoading;
	}

	@Override
	public List<EntryDetails> getEntries() {
		return entries;
	}

	@Override
	public void onEntrySelected(EntryDetails entry) {
		activeEntry = entry;

		updateSelections();
	}

	private void updateSelections() {
		selections = null;

		if (activeEntry == null) {
			getView().refreshSelections();
			return;
		}

		updateLoading(entriesLoading, true);

		dispatcher.execute(new GetSelectionsAction(activeEntry), new AsyncCallback<GetSelectionsResult>() {
			@Override
			public void onFailure(Throwable error) {
				updateLoading(entriesLoading, false);
				handleError(error);
			}

			@Override
			public void onSuccess(GetSelectionsResult result) {
				updateLoading(entriesLoading, false);
				selections = result.getSelections();
				getView().refreshSelections();
			}
		});
	}

	@Override
	public List<TeamDetails> getSelections() {
		return selections;
	}
}