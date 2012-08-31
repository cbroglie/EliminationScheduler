package com.cbroglie.eliminationscheduler.server;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.DeleteEntryAction;
import com.cbroglie.eliminationscheduler.shared.DeleteEntryResult;
import com.cbroglie.eliminationscheduler.shared.model.Entry;

import com.google.inject.Inject;

import com.googlecode.objectify.Objectify;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class DeleteEntryHandler implements ActionHandler<DeleteEntryAction, DeleteEntryResult> {

	private final DAOProvider dao;

	@Inject
	public DeleteEntryHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public DeleteEntryResult execute(DeleteEntryAction action, ExecutionContext context) throws ActionException {

		Objectify ofy = dao.get().ofy();
		Entry entry = ofy.get(Entry.class, action.getEntry().getId()); 

		if (!UserValidator.isCurrentUser(entry.getUserId())) {
			throw new ActionException("The user is not logged in");
		}

		ofy.delete(entry);

		return new DeleteEntryResult(true);
	}

	@Override
	public Class<DeleteEntryAction> getActionType() {
		return DeleteEntryAction.class;
	}

	@Override
	public void undo(DeleteEntryAction action, DeleteEntryResult result, ExecutionContext context) throws ActionException {
		// No undo once the entry is deleted.
	}
}
