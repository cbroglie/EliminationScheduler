package com.cbroglie.eliminationscheduler.client.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.LoginGatekeeper;
import com.cbroglie.eliminationscheduler.client.NameTokens;
import com.cbroglie.eliminationscheduler.client.event.LoadingEvent;
import com.cbroglie.eliminationscheduler.client.view.ScheduleUiHandlers;
import com.cbroglie.eliminationscheduler.shared.GetEntriesAction;
import com.cbroglie.eliminationscheduler.shared.GetEntriesResult;
import com.cbroglie.eliminationscheduler.shared.GetTeamScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamScheduleResult;
import com.cbroglie.eliminationscheduler.shared.GetTeamsAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamsResult;
import com.cbroglie.eliminationscheduler.shared.GetWeeklyScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetWeeklyScheduleResult;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsAction;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsResult;
import com.cbroglie.eliminationscheduler.shared.NFLConstants;
import com.cbroglie.eliminationscheduler.shared.SetSelectionAction;
import com.cbroglie.eliminationscheduler.shared.SetSelectionResult;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class SchedulePresenter extends Presenter<SchedulePresenter.MyView, SchedulePresenter.MyProxy> implements ScheduleUiHandlers {

	public interface MyView extends View, HasUiHandlers<ScheduleUiHandlers> {
		void refreshWeeklySchedule();
		void refreshTeamSchedule();
		void refreshEntries();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.schedulePage)
	@UseGatekeeper(LoginGatekeeper.class)
	public interface MyProxy extends ProxyPlace<SchedulePresenter> {}

	public static final String TOKEN_WEEK = "week";
	public static final String TOKEN_ENTRY = "entry";
	public static final String TOKEN_TEAM = "team";

	private final DispatchAsync dispatcher;
	private final PlaceManager placeManager;
	private final CurrentUser currentUser;

	private int currentWeek = 0;
	private List<GameDetails> games = null;
	private TeamDetails selectedTeam = null;
	private Set<TeamDetails> excludedTeams = new HashSet<TeamDetails>();

	private class TeamScheduleGame {
		public TeamDetails opponent;
		public boolean isHome;
	}

	private TeamDetails selectedTeamScheduleTeam = null;
	private TeamScheduleGame teamScheduleGames[] = null;
	private List<TeamDetails> teams = null;

	private EntryDetails activeEntry = null;
	private List<EntryDetails> entries = null;
	private Long requestedEntryId = null; // from the place request url

	private boolean scheduleLoading = false;
	private boolean selectionsLoading = false;
	private boolean teamsLoading = false;
	private boolean entriesLoading = false;
	private boolean teamScheduleLoading = false;

	@Inject
	public SchedulePresenter(
			final EventBus eventBus,
			final MyView view,
			final MyProxy proxy,
			final PlaceManager placeManager,
			final DispatchAsync dispatcher,
			final CurrentUser currentUser) {

		super(eventBus, view, proxy);

		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.currentUser = currentUser;

		getView().setUiHandlers(this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Clear all cached data that could have been modified externally.
		activeEntry = null;
		entries = null;
		selectedTeam = null;
		excludedTeams.clear();

		// Reset all loading variables.
		updateLoading(false, false, false, false, false);

		// Refresh the view.
		updateSchedule();
		updateTeams();
		updateEntries();
	}

	@Override
	protected void onHide() {
		super.onHide();

		// Hide the loading screen if active.
		updateLoading(false, false, false, false, false);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		// Check the place request for the current week.
		String weekString = request.getParameter(TOKEN_WEEK, "0");
		try {
			currentWeek = Integer.parseInt(weekString);
		}
		catch (NumberFormatException e) {
			currentWeek = 0;
		}
		finally {
			currentWeek = Math.max(0, Math.min(currentWeek, NFLConstants.WEEKS_PER_SEASON - 1));
		}

		// Check the place request for the active entry.
		requestedEntryId = null;
		String entryString = request.getParameter(TOKEN_ENTRY, null);
		if (entryString != null) {
			try {
				requestedEntryId = Long.parseLong(entryString);
			}
			catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public void onTeamSelected(TeamDetails team) {
		if (activeEntry == null) {
			return;
		}

		// Update which team is selected.
		if (team.equals(selectedTeam)) {
			selectedTeam = null;
			excludedTeams.remove(team);
		}
		else {
			selectedTeam = team;
			excludedTeams.add(team);
		}

		updateLoading(scheduleLoading, true, teamsLoading, entriesLoading, teamScheduleLoading);

		dispatcher.execute(new SetSelectionAction(activeEntry, currentWeek, selectedTeam), new AsyncCallback<SetSelectionResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
				updateLoading(scheduleLoading, false, teamsLoading, entriesLoading, teamScheduleLoading);

				// Now fetch the updated selections.
				updateSelections();
			}

			@Override
			public void onSuccess(SetSelectionResult result) {
				updateLoading(scheduleLoading, false, teamsLoading, entriesLoading, teamScheduleLoading);

				// Now fetch the updated selections.
				updateSelections();
			}
		});
	}

	private void updateSchedule() {
		updateLoading(true, selectionsLoading, teamsLoading, entriesLoading, teamScheduleLoading);

		dispatcher.execute(new GetWeeklyScheduleAction(currentWeek), new AsyncCallback<GetWeeklyScheduleResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);

				games = null;
				getView().refreshWeeklySchedule();

				updateLoading(false, selectionsLoading, teamsLoading, entriesLoading, teamScheduleLoading);
			}

			@Override
			public void onSuccess(GetWeeklyScheduleResult result) {
				games = result.getGames();
				getView().refreshWeeklySchedule();

				updateLoading(false, selectionsLoading, teamsLoading, entriesLoading, teamScheduleLoading);
			}
		});
	}

	private void updateSelections() {
		if (activeEntry == null) {
			updateSelections(null);
			return;
		}

		updateLoading(scheduleLoading, true, teamsLoading, entriesLoading, teamScheduleLoading);

		dispatcher.execute(new GetSelectionsAction(activeEntry), new AsyncCallback<GetSelectionsResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
				updateLoading(scheduleLoading, false, teamsLoading, entriesLoading, teamScheduleLoading);
			}

			@Override
			public void onSuccess(GetSelectionsResult result) {
				updateSelections(result.getSelections());
				updateLoading(scheduleLoading, false, teamsLoading, entriesLoading, teamScheduleLoading);
			}
		});
	}

	private void updateTeams() {
		updateLoading(scheduleLoading, selectionsLoading, true, entriesLoading, teamScheduleLoading);

		dispatcher.execute(new GetTeamsAction(), new AsyncCallback<GetTeamsResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);

				teams = null;
				selectedTeamScheduleTeam = null;

				getView().refreshTeamSchedule();

				updateLoading(scheduleLoading, selectionsLoading, false, entriesLoading, teamScheduleLoading);

				updateTeamSchedule();
			}

			@Override
			public void onSuccess(GetTeamsResult result) {
				teams = result.getTeams();

				getView().refreshTeamSchedule();

				updateLoading(scheduleLoading, selectionsLoading, false, entriesLoading, teamScheduleLoading);

				updateTeamSchedule();
			}
		});
	}

	private void updateTeamSchedule() {
		if (selectedTeamScheduleTeam == null) {
			getView().refreshTeamSchedule();
			return;
		}

		updateLoading(scheduleLoading, selectionsLoading, teamsLoading, entriesLoading, true);

		dispatcher.execute(new GetTeamScheduleAction(selectedTeamScheduleTeam), new AsyncCallback<GetTeamScheduleResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);

				teamScheduleGames = null;

				getView().refreshTeamSchedule();

				updateLoading(scheduleLoading, selectionsLoading, teamsLoading, entriesLoading, false);
			}

			@Override
			public void onSuccess(GetTeamScheduleResult result) {
				teamScheduleGames = new TeamScheduleGame[NFLConstants.WEEKS_PER_SEASON];
				for (int weekIdx = 0; weekIdx < NFLConstants.WEEKS_PER_SEASON; weekIdx++) {
					teamScheduleGames[weekIdx] = new TeamScheduleGame();
					teamScheduleGames[weekIdx].opponent = null;
				}

				for (GameDetails game : result.getGames()) {
					int week = game.getWeek();
					if (game.getHomeTeam().equals(selectedTeamScheduleTeam)) {
						teamScheduleGames[week].opponent = game.getAwayTeam();
						teamScheduleGames[week].isHome = true;
					}
					else {
						teamScheduleGames[week].opponent = game.getHomeTeam();
						teamScheduleGames[week].isHome = false;
					}
				}

				getView().refreshTeamSchedule();

				updateLoading(scheduleLoading, selectionsLoading, teamsLoading, entriesLoading, false);
			}
		});
	}

	private void updateEntries() {
		updateLoading(scheduleLoading, selectionsLoading, teamsLoading, true, teamScheduleLoading);

		dispatcher.execute(new GetEntriesAction(currentUser.getUserId()), new AsyncCallback<GetEntriesResult>() {
			@Override
			public void onFailure(Throwable error) {
				handleError(error);

				entries = null;

				getView().refreshEntries();

				updateLoading(scheduleLoading, selectionsLoading, teamsLoading, false, teamScheduleLoading);

				updateSelections();
			}

			@Override
			public void onSuccess(GetEntriesResult result) {
				entries = result.getEntries();

				if (requestedEntryId != null) {
					for (EntryDetails entry : entries) {
						if (entry.getId().equals(requestedEntryId)) {
							activeEntry = entry;
						}
					}
				}
				
				getView().refreshEntries();

				updateLoading(scheduleLoading, selectionsLoading, teamsLoading, false, teamScheduleLoading);

				updateSelections();
			}
		});
	}

	private void updateLoading(boolean scheduleLoading, boolean selectionsLoading, boolean teamsLoading, boolean entriesLoading, boolean teamScheduleLoading) {
		boolean wasLoading = (this.scheduleLoading || this.selectionsLoading || this.teamsLoading || this.entriesLoading || this.teamScheduleLoading);
		boolean isLoading = (scheduleLoading || selectionsLoading || teamsLoading || entriesLoading || teamScheduleLoading);

		if (isLoading != wasLoading) {
			LoadingEvent.fire(this, !isLoading);
		}

		this.scheduleLoading = scheduleLoading;
		this.selectionsLoading = selectionsLoading;
		this.teamsLoading = teamsLoading;
		this.entriesLoading = entriesLoading;
		this.teamScheduleLoading = teamScheduleLoading;
	}

	private void updateSelections(ArrayList<TeamDetails> selections) {
		excludedTeams.clear();
		selectedTeam = null;
		if (selections != null) {
			int numWeeks = selections.size();
			for (int weekIdx = 0; weekIdx < numWeeks; weekIdx++) {
				// If the user has selected a team for this week, add the team
				// to the excluded list.
				TeamDetails team = selections.get(weekIdx);
				if (team != null) {
					excludedTeams.add(team);

					// Store the selected team for the current week. 
					if (weekIdx == currentWeek) {
						selectedTeam = team;
					}
				}
			}
		}

		getView().refreshWeeklySchedule();
	}

	private void handleError(Throwable error) {
		error.printStackTrace();
		if (error.getMessage() != null) {
			Window.alert(error.getMessage());
		}
		else {
			Window.alert("Internal error");
		}
	}

	private void refreshPlace() {
		PlaceRequest request = new PlaceRequest(NameTokens.schedulePage).with(TOKEN_WEEK, Integer.toString(currentWeek));

		// Add the active entry to the place request.
		if (activeEntry != null) {
			request = request.with(TOKEN_ENTRY, activeEntry.getId().toString());
		}

		placeManager.revealRelativePlace(request, -1);
	}

	@Override
	public void onWeekSelected(int week) {
		currentWeek = week;
		refreshPlace();
	}

	@Override
	public boolean canSelectTeam(TeamDetails team) {
		// Allow user to unselect the currently selected team.
		if (team.equals(selectedTeam)) {
			return true;
		}

		return !excludedTeams.contains(team);
	}

	@Override
	public boolean isTeamSelected(TeamDetails team) {
		return team.equals(selectedTeam);
	}

	@Override
	public int getCurrentWeek() {
		return currentWeek;
	}

	@Override
	public List<GameDetails> getGames() {
		return games;
	}

	@Override
	public void onTeamScheduleTeamSelected(TeamDetails team) {
		selectedTeamScheduleTeam = team;
		updateTeamSchedule();
	}

	@Override
	public TeamDetails getTeamScheduleOppoentForWeek(int week) {
		if (teamScheduleGames == null || (week >= teamScheduleGames.length)) {
			return null;
		}

		return teamScheduleGames[week].opponent;
	}

	@Override
	public boolean getTeamScheduleIsHomeForWeek(int week) {
		if (teamScheduleGames == null || (week >= teamScheduleGames.length)) {
			return false;
		}

		return teamScheduleGames[week].isHome;
	}

	@Override
	public List<TeamDetails> getTeams() {
		return teams;
	}

	@Override
	public List<EntryDetails> getEntries() {
		return entries;
	}

	@Override
	public void onEntrySelected(EntryDetails entry) {
		activeEntry = entry;
		refreshPlace();
	}

	@Override
	public TeamDetails getSelectedTeamScheduleTeam() {
		return selectedTeamScheduleTeam;
	}

	@Override
	public EntryDetails getSelectedEntry() {
		return activeEntry;
	}
}
