package com.cbroglie.eliminationscheduler.server;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsAction;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsResult;
import com.cbroglie.eliminationscheduler.shared.NFLConstants;
import com.cbroglie.eliminationscheduler.shared.model.Entry;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetSelectionsHandler implements ActionHandler<GetSelectionsAction, GetSelectionsResult> {

	private final DAOProvider dao;

	@Inject
	public GetSelectionsHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public GetSelectionsResult execute(GetSelectionsAction action, ExecutionContext context) throws ActionException {

		ArrayList<TeamDetails> selections = new ArrayList<TeamDetails>();

		Objectify ofy = dao.get().ofy();

		Entry entry = ofy.get(Entry.class, action.getEntry().getId());
		if (entry == null) {
			throw new ActionException("No active entry");
		}
		else if (!UserValidator.isCurrentUser(entry.getUserId())) {
			throw new ActionException("The user is not logged in");
		}

		for (int week = 0; week < NFLConstants.WEEKS_PER_SEASON; week++) {
			Key<Team> teamKey = entry.getSelection(week);
			if (teamKey == null) {
				selections.add(null);
			}
			else {
				Team team = ofy.get(teamKey);

				selections.add(new TeamDetails(team.getId(), team.getDisplayName()));
			}
		}

		return new GetSelectionsResult(selections);
	}

	@Override
	public Class<GetSelectionsAction> getActionType() {
		return GetSelectionsAction.class;
	}

	@Override
	public void undo(GetSelectionsAction action, GetSelectionsResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}
}
