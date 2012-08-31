package com.cbroglie.eliminationscheduler.client.view;

import java.util.ArrayList;
import java.util.List;

import com.cbroglie.eliminationscheduler.client.presenter.SchedulePresenter;
import com.cbroglie.eliminationscheduler.client.ui.SelectOneListBox;
import com.cbroglie.eliminationscheduler.shared.NFLConstants;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class ScheduleView extends CompositeViewWithUiHandlers<ScheduleUiHandlers> implements SchedulePresenter.MyView {

	private static ScheduleViewUiBinder uiBinder = GWT.create(ScheduleViewUiBinder.class);

	interface ScheduleViewUiBinder extends UiBinder<Widget, ScheduleView> {}

	interface Style extends CssResource {
		String currentWeek();
		String defaultWeek();
		String selectedTeam();
		String excludedTeam();
		String defaultTeam();
		String teamSchedule();
	}

	@UiField
	Style style;

	@UiField(provided = true)
	final Grid weeksTable;

	@UiField
	FlexTable scheduleTable;

	@UiField(provided = true)
	final SelectOneListBox<TeamDetails> teamScheduleListBox;

	@UiField
	FlexTable teamScheduleTable;

	@UiField(provided = true)
	final SelectOneListBox<EntryDetails> entriesListBox;

	private static final int GAMES_PER_COLUMN = 4;

	public ScheduleView() {
		this.weeksTable = new Grid(1, NFLConstants.WEEKS_PER_SEASON);

		// Setup the team schedule list box. 
		this.teamScheduleListBox = new SelectOneListBox<TeamDetails>(new SelectOneListBox.ItemFormatter<TeamDetails>() {
			@Override
			public String getLabel(TeamDetails item) {
				if (item == null) {
					return "Select team"; 
				}

				return item.getDisplayName();
			}

			@Override
			public String getValue(TeamDetails item) {
				return getLabel(item);
			}
		});

		this.teamScheduleListBox.addValueChangeHandler(new ValueChangeHandler<TeamDetails>() {
			@Override
			public void onValueChange(ValueChangeEvent<TeamDetails> event) {
				if (getUiHandlers() != null) {
					getUiHandlers().onTeamScheduleTeamSelected(event.getValue());
				}
			}
		});

		// Setup the entries list box.
		this.entriesListBox = new SelectOneListBox<EntryDetails>(new SelectOneListBox.ItemFormatter<EntryDetails>() {
			@Override
			public String getLabel(EntryDetails item) {
				if (item == null) {
					return "Select an entry"; 
				}

				return item.getName();
			}

			@Override
			public String getValue(EntryDetails item) {
				return getLabel(item);
			}
		});

		this.entriesListBox.addValueChangeHandler(new ValueChangeHandler<EntryDetails>() {
			@Override
			public void onValueChange(ValueChangeEvent<EntryDetails> event) {
				if (getUiHandlers() != null) {
					getUiHandlers().onEntrySelected(event.getValue());
				}
			}
		});

		initWidget(uiBinder.createAndBindUi(this));

		refreshTeamScheduleItems(null, null);
		refreshWeeksTable();
		refreshEntriesItems(null, null);
	}

	private void refreshTeamScheduleItems(List<TeamDetails> teams, TeamDetails selectedTeam) {
		ArrayList<TeamDetails> items = new ArrayList<TeamDetails>();

		// Insert a null team at index 0 to show "select one" 
		items.add(0, null);

		if (teams != null) {
			for (TeamDetails team : teams) {
				items.add(team);		
			}
		}

		this.teamScheduleListBox.setItems(items);

		if (selectedTeam != null) {
			this.teamScheduleListBox.setSelectedItem(selectedTeam);
		}
	}

	/**
	 * Helper method to set the text for a team schedule table cell and apply
	 * the proper style.
	 */
	private void setScheduleTableCell(int row, int col, String text) {
		teamScheduleTable.setText(row, col, text);
		teamScheduleTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		teamScheduleTable.getCellFormatter().addStyleName(row, col, style.teamSchedule());
	}

	private void refreshTeamScheduleTable() {
		if (getUiHandlers() == null) {
			refreshTeamScheduleItems(null, null);
		}
		else {
			refreshTeamScheduleItems(getUiHandlers().getTeams(), getUiHandlers().getSelectedTeamScheduleTeam());

			teamScheduleTable.removeAllRows();
			if (teamScheduleListBox.getSelectedItem() != null) {
				for (int weekIdx = 0; weekIdx < NFLConstants.WEEKS_PER_SEASON; weekIdx++) {
					TeamDetails opponent = getUiHandlers().getTeamScheduleOppoentForWeek(weekIdx);
					
					String text = "BYE";
					if (opponent != null) {
						boolean isHome = getUiHandlers().getTeamScheduleIsHomeForWeek(weekIdx);
						if (isHome) {
							text = opponent.getDisplayName();
						}
						else {
							text = "@" + opponent.getDisplayName();
						}
					}
	
					setScheduleTableCell(weekIdx, 0, Integer.toString(weekIdx + 1));
					setScheduleTableCell(weekIdx, 1, text);
				}
			}
		}
	}

	private void refreshEntriesItems(List<EntryDetails> entries, EntryDetails selectedEntry) {
		ArrayList<EntryDetails> items = new ArrayList<EntryDetails>();

		// Insert a null team at index 0 to show "select an entry" 
		items.add(0, null);

		if (entries != null) {
			for (EntryDetails entry : entries) {
				items.add(entry);		
			}
		}

		this.entriesListBox.setItems(items);

		if (selectedEntry != null) {
			this.entriesListBox.setSelectedItem(selectedEntry);
		}
	}

	private void refreshWeeksTable() {
		int currentWeek = getCurrentWeek();

		for (int weekIdx = 0; weekIdx < NFLConstants.WEEKS_PER_SEASON; weekIdx++) {
			setWeeksCell(0, weekIdx, Integer.toString(weekIdx + 1), (weekIdx == currentWeek));
		}
	}

	/**
	 * Helper method to set the text for a weeks table cell and apply the proper
	 * style.
	 */
	private void setWeeksCell(int row, int col, String text, boolean isCurrentWeek) {
		weeksTable.setText(row, col, text);
		weeksTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		if (isCurrentWeek) {
			weeksTable.getCellFormatter().addStyleName(row, col, style.currentWeek());
			weeksTable.getCellFormatter().removeStyleName(row, col, style.defaultWeek());
		}
		else {
			weeksTable.getCellFormatter().addStyleName(row, col, style.defaultWeek());
			weeksTable.getCellFormatter().removeStyleName(row, col, style.currentWeek());	
		}
	}

	/**
	 * Helper method to set the text for a schedule table cell and apply the proper style.
	 */
	private void setScheduleCell(int row, int col, String text, boolean isSelected, boolean canSelect) {
		scheduleTable.setText(row, col, text);
		scheduleTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		if (isSelected) {
			scheduleTable.getCellFormatter().addStyleName(row, col, style.selectedTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.excludedTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.defaultTeam());
		}
		else if (!canSelect) {
			scheduleTable.getCellFormatter().addStyleName(row, col, style.excludedTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.selectedTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.defaultTeam());
		}
		else {
			scheduleTable.getCellFormatter().addStyleName(row, col, style.defaultTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.selectedTeam());
			scheduleTable.getCellFormatter().removeStyleName(row, col, style.excludedTeam());
		}
	}

	private void refreshScheduleTable() {
		// Populate the given games.
		int gameIdx = 0;
		List<GameDetails> games = getGames();
		if (games != null) {
			for (GameDetails game : games) {
				for (int i = 0; i < 2; i++) {
					boolean isHome = (i == 0);

					TeamDetails team = (isHome ? game.getHomeTeam() : game.getAwayTeam()); 

					boolean isSelected = false;
					boolean canSelect = true;
					if (getUiHandlers() != null) {
						isSelected = getUiHandlers().isTeamSelected(team);
						canSelect = getUiHandlers().canSelectTeam(team);
					}

					String text = (isHome ? "@ " + team.getDisplayName() : team.getDisplayName());

					int row = rowFromGameIdx(gameIdx, isHome);
					int col = colFromGameIdx(gameIdx, isHome);

					setScheduleCell(row, col, text, isSelected, canSelect);	
				}

				gameIdx++;
			}
		}

		// Clear out any remaining cells (in case of bye weeks).
		for (; gameIdx < NFLConstants.MAX_GAMES_PER_WEEK; gameIdx++) {
			for (int i = 0; i < 2; i++) {
				boolean isHome = (i == 0);
				
				int row = rowFromGameIdx(gameIdx, isHome);
				int col = colFromGameIdx(gameIdx, isHome);

				setScheduleCell(row, col, "", false, true);
			}
		}
	}

	private int getCurrentWeek() {
		return (getUiHandlers() != null ? getUiHandlers().getCurrentWeek() : 0);
	}

	private List<GameDetails> getGames() {
		return (getUiHandlers() != null ? getUiHandlers().getGames() : null);
	}

	private int gameIdxFromRowAndCol(int row, int col) {
		int gameIdx = (((row / 2) * GAMES_PER_COLUMN) + col);
		
		return gameIdx;
	}
	
	private boolean isHomeFromRowAndCol(int row, int col) {
		boolean isHome = ((row & 1) != 0);
		
		return isHome;
	}

	private int rowFromGameIdx(int gameIdx, boolean isHome) {
		int row = ((gameIdx / GAMES_PER_COLUMN) * 2);
		if (isHome) {
			row++;
		}
		
		return row;
	}
	
	private int colFromGameIdx(int gameIdx, boolean isHome) {
		int col = (gameIdx % GAMES_PER_COLUMN);
		
		return col;
	}

	@UiHandler("weeksTable")
	void onWeeksTableClicked(ClickEvent event) {
		HTMLTable.Cell cell = weeksTable.getCellForEvent(event);
		if (cell == null) {
			return;
		}

		assert(cell.getRowIndex() == 0);

		int currentWeek = cell.getCellIndex();

		if (getUiHandlers() != null) {
			getUiHandlers().onWeekSelected(currentWeek);
		}
	}

	@UiHandler("scheduleTable")
	void onScheduleTableClicked(ClickEvent event) {		
		if (getUiHandlers() != null) {
			List<GameDetails> games = getGames();
			if (games == null) {
				return;
			}

			HTMLTable.Cell cell = scheduleTable.getCellForEvent(event);
			if (cell == null) {
				return;
			}

			int row = cell.getRowIndex();
			int col = cell.getCellIndex();

			int gameIdx = gameIdxFromRowAndCol(row, col);
			boolean isHome = isHomeFromRowAndCol(row, col);

			GameDetails game = games.get(gameIdx);
			TeamDetails team = (isHome ? game.getHomeTeam() : game.getAwayTeam());

			getUiHandlers().onTeamSelected(team);
		}
	}

	@Override
	public void refreshWeeklySchedule() {
		refreshWeeksTable();
		refreshScheduleTable();
	}

	@Override
	public void refreshTeamSchedule() {
		refreshTeamScheduleTable();		
	}

	@Override
	public void refreshEntries() {
		if (getUiHandlers() == null) {
			refreshEntriesItems(null, null);
		}
		else {
			refreshEntriesItems(getUiHandlers().getEntries(), getUiHandlers().getSelectedEntry());
		}
	}

	@Override
	public Widget asWidget() {
		return this;
	}
}
