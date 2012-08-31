package com.cbroglie.eliminationscheduler.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.LoginGatekeeper;
import com.cbroglie.eliminationscheduler.client.NameTokens;
import com.cbroglie.eliminationscheduler.client.view.ManageEntriesUiHandlers;
import com.cbroglie.eliminationscheduler.shared.CreateEntryAction;
import com.cbroglie.eliminationscheduler.shared.CreateEntryResult;
import com.cbroglie.eliminationscheduler.shared.DeleteEntryAction;
import com.cbroglie.eliminationscheduler.shared.DeleteEntryResult;
import com.cbroglie.eliminationscheduler.shared.GetEntriesAction;
import com.cbroglie.eliminationscheduler.shared.GetEntriesResult;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

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

public class ManageEntriesPresenter extends Presenter<ManageEntriesPresenter.MyView, ManageEntriesPresenter.MyProxy> implements ManageEntriesUiHandlers {

	public interface MyView extends View, HasUiHandlers<ManageEntriesUiHandlers> {
		void refreshEntries();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.manageEntriesPage)
	@UseGatekeeper(LoginGatekeeper.class)
	public interface MyProxy extends ProxyPlace<ManageEntriesPresenter> {}

	private final DispatchAsync dispatcher;
	private final CurrentUser currentUser;

	private List<EntryDetails> entries;

	@Inject
	public ManageEntriesPresenter(
			final EventBus eventBus,
			final MyView view,
			final MyProxy proxy,
			final DispatchAsync dispatcher,
			final CurrentUser currentUser) {

		super(eventBus, view, proxy);

		this.dispatcher = dispatcher;
		this.currentUser = currentUser;
		this.entries = new ArrayList<EntryDetails>();

		getView().setUiHandlers(this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		updateEntries();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void createEntry(String name) {
		dispatcher.execute(new CreateEntryAction(currentUser.getUserId(), name), new AsyncCallback<CreateEntryResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
				updateEntries();
			}

			@Override
			public void onSuccess(CreateEntryResult result) {
				updateEntries();
			}
		});
	}

	@Override
	public void deleteEntry(EntryDetails entry) {
		dispatcher.execute(new DeleteEntryAction(entry), new AsyncCallback<DeleteEntryResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
				updateEntries();
			}

			@Override
			public void onSuccess(DeleteEntryResult result) {
				updateEntries();
			}
		});
	}

	@Override
	public List<EntryDetails> getEntries() {
		return entries;
	}

	private void updateEntries() {
		dispatcher.execute(new GetEntriesAction(currentUser.getUserId()), new AsyncCallback<GetEntriesResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}

			@Override
			public void onSuccess(GetEntriesResult result) {
				entries = result.getEntries();

				getView().refreshEntries();
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
}
