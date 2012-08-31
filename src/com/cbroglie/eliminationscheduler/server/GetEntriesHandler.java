package com.cbroglie.eliminationscheduler.server;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.GetEntriesAction;
import com.cbroglie.eliminationscheduler.shared.GetEntriesResult;
import com.cbroglie.eliminationscheduler.shared.model.Entry;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

import com.google.inject.Inject;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetEntriesHandler implements ActionHandler<GetEntriesAction, GetEntriesResult> {

	private final DAOProvider dao;

	@Inject
	public GetEntriesHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public GetEntriesResult execute(GetEntriesAction action, ExecutionContext context) throws ActionException {

		if (!UserValidator.isCurrentUser(action.getUserId())) {
			throw new ActionException("The user is not logged in");
		}

		ArrayList<EntryDetails> entries = new ArrayList<EntryDetails>();

		Objectify ofy = dao.get().ofy();

		Query<Entry> query = ofy.query(Entry.class).filter("userId", action.getUserId());
		for (Entry entry : query) {
			entries.add(new EntryDetails(entry.getId(), entry.getName()));
		}

		return new GetEntriesResult(entries);
	}

	@Override
	public Class<GetEntriesAction> getActionType() {
		return GetEntriesAction.class;
	}

	@Override
	public void undo(GetEntriesAction action, GetEntriesResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}
}
