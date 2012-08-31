package com.cbroglie.eliminationscheduler.server;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.CreateEntryAction;
import com.cbroglie.eliminationscheduler.shared.CreateEntryResult;
import com.cbroglie.eliminationscheduler.shared.FieldVerifier;
import com.cbroglie.eliminationscheduler.shared.model.Entry;

import com.google.inject.Inject;

import com.googlecode.objectify.Objectify;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreateEntryHandler implements ActionHandler<CreateEntryAction, CreateEntryResult> {

	private final DAOProvider dao;

	@Inject
	public CreateEntryHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public CreateEntryResult execute(CreateEntryAction action, ExecutionContext context) throws ActionException {

		if (!UserValidator.isCurrentUser(action.getUserId())) {
			throw new ActionException("The user is not logged in");
		}
		else if (!FieldVerifier.isValidName(action.getName())) {
			throw new ActionException("Invalid entry name");
		}

		Entry entry = new Entry(action.getUserId(), action.getName());

		Objectify ofy = dao.get().ofy();
		ofy.put(entry);

		return new CreateEntryResult(true);
	}

	@Override
	public Class<CreateEntryAction> getActionType() {
		return CreateEntryAction.class;
	}

	@Override
	public void undo(CreateEntryAction action, CreateEntryResult result, ExecutionContext context) throws ActionException {
		// No undo once the entry is created.
	}
}
