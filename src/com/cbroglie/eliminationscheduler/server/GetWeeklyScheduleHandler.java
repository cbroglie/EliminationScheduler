package com.cbroglie.eliminationscheduler.server;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.GetWeeklyScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetWeeklyScheduleResult;
import com.cbroglie.eliminationscheduler.shared.model.Game;
import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.inject.Inject;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetWeeklyScheduleHandler implements ActionHandler<GetWeeklyScheduleAction, GetWeeklyScheduleResult> {

	private final DAOProvider dao;

	@Inject
	public GetWeeklyScheduleHandler(final DAOProvider dao) {
		this.dao = dao;
	}

	@Override
	public GetWeeklyScheduleResult execute(GetWeeklyScheduleAction action, ExecutionContext context) throws ActionException {
		ArrayList<GameDetails> games = new ArrayList<GameDetails>();

		int week = action.getWeek();

		Objectify ofy = dao.get().ofy();

		// TODO: Consider switching to a batch get
		Query<Game> query = ofy.query(Game.class).filter("week", week + 1); // client code operates on weeks 0-16, stored as weeks 1-17
 
		for (Game game : query) {
			assert(week + 1 == game.getWeek());

			Team homeTeam = ofy.get(game.getHomeTeam());
			Team awayTeam = ofy.get(game.getAwayTeam());

			TeamDetails homeTeamDetails = new TeamDetails(homeTeam.getId(), homeTeam.getDisplayName());
			TeamDetails awayTeamDetails = new TeamDetails(awayTeam.getId(), awayTeam.getDisplayName());

			games.add(new GameDetails(game.getId(), week, homeTeamDetails, awayTeamDetails));
		}

		return new GetWeeklyScheduleResult(games);
	}

	@Override
	public Class<GetWeeklyScheduleAction> getActionType() {
		return GetWeeklyScheduleAction.class;
	}

	@Override
	public void undo(GetWeeklyScheduleAction action, GetWeeklyScheduleResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}
}
