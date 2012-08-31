package com.cbroglie.eliminationscheduler.client.view;

import java.util.List;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ScheduleUiHandlers extends UiHandlers {
	// Callbacks for operating on the weekly schedule.
	void onTeamSelected(TeamDetails team);
	void onWeekSelected(int week);
	List<GameDetails> getGames();
	int getCurrentWeek();
	boolean canSelectTeam(TeamDetails team);
	boolean isTeamSelected(TeamDetails team);

	// Callbacks for operating on the team schedule.
	List<TeamDetails> getTeams();
	TeamDetails getSelectedTeamScheduleTeam();
	void onTeamScheduleTeamSelected(TeamDetails team);
	TeamDetails getTeamScheduleOppoentForWeek(int week);
	boolean getTeamScheduleIsHomeForWeek(int week);

	// Callbacks for operating on the entries.
	List<EntryDetails> getEntries();
	EntryDetails getSelectedEntry();
	void onEntrySelected(EntryDetails entry);
}
