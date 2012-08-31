package com.cbroglie.eliminationscheduler.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.GetTeamsAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamsResult;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.inject.Inject;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTeamsHandler implements ActionHandler<GetTeamsAction, GetTeamsResult> {

	private final DAOProvider dao;

	@Inject
	public GetTeamsHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public GetTeamsResult execute(GetTeamsAction action, ExecutionContext context) throws ActionException {
		ArrayList<TeamDetails> teams = new ArrayList<TeamDetails>();

		Objectify ofy = dao.get().ofy();

		// Get all of the teams.
		// TODO: Consider switching to a batch get
		Query<Team> query = ofy.query(Team.class);
		for (Team team : query) {
			teams.add(new TeamDetails(team.getId(), team.getDisplayName()));
		}

		// Now sort the games by alphabetical order.
		Collections.sort(teams, new Comparator<TeamDetails>() {
			@Override
			public int compare(TeamDetails lhs, TeamDetails rhs){
				return lhs.getDisplayName().compareTo(rhs.getDisplayName());
			}
		});

		return new GetTeamsResult(teams);
	}

	@Override
	public Class<GetTeamsAction> getActionType() {
		return GetTeamsAction.class;
	}

	@Override
	public void undo(GetTeamsAction action, GetTeamsResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}

}
