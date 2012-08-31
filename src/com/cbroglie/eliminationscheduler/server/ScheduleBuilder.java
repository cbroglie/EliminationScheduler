package com.cbroglie.eliminationscheduler.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.fileupload.FileItem;

import com.cbroglie.eliminationscheduler.server.guice.DAOProvider;
import com.cbroglie.eliminationscheduler.shared.NFLConstants;
import com.cbroglie.eliminationscheduler.shared.model.Game;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

@Singleton
public final class ScheduleBuilder {

	private final DAOProvider dao;

	@Inject
	public ScheduleBuilder(final DAOProvider dao) {
		this.dao = dao;
	}

	public void createScheduleFromFile(FileItem scheduleFile) throws InvalidScheduleFileException {

		Objectify ofy = dao.get().ofy();

		// Games store the teams based on the id, and the id is not generated
		// until the teams are saved. And since we don't want to store the teams
		// until we have verified all of the input, this temporary class is used
		// to store the games.
		class LocalGame {
			public final int week;
			public final Date date;
			public final Team homeTeam;
			public final Team awayTeam;

			public LocalGame(int week, Date date, Team homeTeam, Team awayTeam) {
				this.week = week;
				this.date = date;
				this.homeTeam = homeTeam;
				this.awayTeam = awayTeam;
			}
		}

		Map<String, Team> teams = new HashMap<String, Team>();
		List<LocalGame> games = new ArrayList<LocalGame>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(scheduleFile.getInputStream()));
			String line = null;

			// Read the full file into memory, then check it's validity.
			while ((line = reader.readLine()) != null) {
				StringTokenizer toks = new StringTokenizer(line, ",");
				
				if (toks.countTokens() != 3) {
					continue;
				}

				int week = Integer.parseInt(toks.nextToken());

				String homeFullName = toks.nextToken();
				String awayFullName = toks.nextToken();

				String awayDisplayName = awayFullName;
				String homeDisplayName = homeFullName;

				Team awayTeam = getTeam(awayFullName, awayDisplayName, teams);
				Team homeTeam = getTeam(homeFullName, homeDisplayName, teams);

				LocalGame game = new LocalGame(week, new Date(), homeTeam, awayTeam);
				games.add(game);
			}

			// Check the number of teams and games.
			if (teams.size() != NFLConstants.NUM_TEAMS) {
				throw new InvalidScheduleFileException("Incomplete schedule file, included " + teams.size() + " teams");
			}

			if (games.size() != NFLConstants.GAMES_PER_SEASON) {
				throw new InvalidScheduleFileException("Incomplete schedule file, included " + games.size() + " games");
			}

			// Input is valid, save the teams and games.
			ofy.put(teams.values());

			for (LocalGame localGame : games) {
				Key<Team> homeTeam = new Key<Team>(Team.class, localGame.homeTeam.getId());
				Key<Team> awayTeam = new Key<Team>(Team.class, localGame.awayTeam.getId());

				Game game = new Game(localGame.week, localGame.date, homeTeam, awayTeam);
				ofy.put(game);
			}
		}
		catch (IOException e) {
			throw new InvalidScheduleFileException(e.getMessage());
		}
	}

	private static Team getTeam(String fullName, String displayName, Map<String, Team> teams) {
		Team team = teams.get(displayName);
		if (team == null) {
			team = new Team(displayName, fullName);

			teams.put(displayName, team);
		}

		return team;
	}
}
