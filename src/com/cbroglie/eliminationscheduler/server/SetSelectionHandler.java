package com.cbroglie.eliminationscheduler.server;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.SetSelectionAction;
import com.cbroglie.eliminationscheduler.shared.SetSelectionResult;
import com.cbroglie.eliminationscheduler.shared.model.Entry;
import com.cbroglie.eliminationscheduler.shared.model.Team;

import com.google.inject.Inject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class SetSelectionHandler implements ActionHandler<SetSelectionAction, SetSelectionResult> {

	private final DAOProvider dao;

	@Inject
	public SetSelectionHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public SetSelectionResult execute(SetSelectionAction action, ExecutionContext context) throws ActionException {

		Objectify ofy = dao.get().ofy();

		Entry entry = ofy.get(Entry.class, action.getEntry().getId());
		if (entry == null) {
			throw new ActionException("No active entry");
		}

		// Update the selection.
		Key<Team> teamKey = (action.getTeam() != null ? new Key<Team>(Team.class, action.getTeam().getId()) : null);
		entry.setSelection(action.getWeek(), teamKey);

		// Update the datastore.
		ofy.put(entry);

		return new SetSelectionResult(true);
	}

	@Override
	public Class<SetSelectionAction> getActionType() {
		return SetSelectionAction.class;
	}

	@Override
	public void undo(SetSelectionAction action, SetSelectionResult result, ExecutionContext context) throws ActionException {
		// TODO: Support undo.
	}
}
