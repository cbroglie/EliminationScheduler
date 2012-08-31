package com.cbroglie.eliminationscheduler.client.view;

import java.util.List;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.gwtplatform.mvp.client.UiHandlers;

public interface MyPicksUiHandlers extends UiHandlers {

	List<EntryDetails> getEntries();
	void onEntrySelected(EntryDetails entry);

	List<TeamDetails> getSelections();
}
