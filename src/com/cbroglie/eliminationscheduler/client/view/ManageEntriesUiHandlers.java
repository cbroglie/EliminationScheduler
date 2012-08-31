package com.cbroglie.eliminationscheduler.client.view;

import java.util.List;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ManageEntriesUiHandlers extends UiHandlers {
	void createEntry(String name);
	void deleteEntry(EntryDetails entry);

	List<EntryDetails> getEntries();
}
