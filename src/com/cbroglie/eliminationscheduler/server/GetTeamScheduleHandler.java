package com.cbroglie.eliminationscheduler.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.GetTeamScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamScheduleResult;
import com.cbroglie.eliminationscheduler.shared.model.Game;
import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetTeamScheduleHandler implements ActionHandler<GetTeamScheduleAction, GetTeamScheduleResult> {

	private final DAOProvider dao;

	@Inject
	public GetTeamScheduleHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public GetTeamScheduleResult execute(GetTeamScheduleAction action, ExecutionContext context) throws ActionException {
		ArrayList<GameDetails> games = new ArrayList<GameDetails>();

		Objectify ofy = dao.get().ofy();

		Key<Team> teamKey = new Key<Team>(Team.class, action.getTeam().getId());

		// We can't use the logical or with 2 different variables, so
		// execute 2 queries: 1 for the home games, 1 for the away games.
		// TODO: Consider switching to a batch get
		for (int i = 0; i < 2; i++) {
			boolean isHome = (i == 0);

			Query<Game> query = null;
			if (isHome) {
				query = ofy.query(Game.class).filter("homeTeam", teamKey);
			}
			else {
				query = ofy.query(Game.class).filter("awayTeam", teamKey);
			}

			for (Game game : query) {
				Team homeTeam = ofy.get(game.getHomeTeam());
				Team awayTeam = ofy.get(game.getAwayTeam());

				TeamDetails homeTeamDetails = new TeamDetails(homeTeam.getId(), homeTeam.getDisplayName());
				TeamDetails awayTeamDetails = new TeamDetails(awayTeam.getId(), awayTeam.getDisplayName());

				int week = game.getWeek() - 1; // client code operates on weeks 0-16, stored as weeks 1-17
				games.add(new GameDetails(game.getId(), week, homeTeamDetails, awayTeamDetails));
			}		
		}

		// Now sort the games by week.
		Collections.sort(games, new Comparator<GameDetails>() {
			@Override
			public int compare(GameDetails lhs, GameDetails rhs){
				return (lhs.getWeek() - rhs.getWeek());
			}
		});

		return new GetTeamScheduleResult(games);
	}

	@Override
	public Class<GetTeamScheduleAction> getActionType() {
		return GetTeamScheduleAction.class;
	}

	@Override
	public void undo(GetTeamScheduleAction action, GetTeamScheduleResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}
}
