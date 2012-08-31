package com.cbroglie.eliminationscheduler.client.view;

import java.util.List;

import com.cbroglie.eliminationscheduler.client.presenter.ManageEntriesPresenter;
import com.cbroglie.eliminationscheduler.shared.FieldVerifier;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageEntriesView extends CompositeViewWithUiHandlers<ManageEntriesUiHandlers> implements ManageEntriesPresenter.MyView {

	private static ManageEntriesUiBinder uiBinder = GWT.create(ManageEntriesUiBinder.class);

	interface ManageEntriesUiBinder extends UiBinder<Widget, ManageEntriesView> {}

	@UiField
	FlexTable entriesTable;

	@UiField
	TextBox addEntryTextBox;

	@UiField
	Button addEntryButton;

	public ManageEntriesView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("addEntryButton")
	void onAddEntryButtonClicked(ClickEvent event) {
		String name = addEntryTextBox.getText();
		if (!FieldVerifier.isValidName(name)) {
			Window.alert("Please enter a valid name");
			return;
		}

		if (getUiHandlers() != null) {
			getUiHandlers().createEntry(name);
		}

		addEntryTextBox.setText("");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void refreshEntries() {
		entriesTable.removeAllRows();

		if (getUiHandlers() != null) {
			List<EntryDetails> entries = getUiHandlers().getEntries();

			int row = 0;
			for (final EntryDetails entry : entries) {
				final Button deleteButton = new Button("x");
				deleteButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						getUiHandlers().deleteEntry(entry);
					}
				});

				entriesTable.setText(row, 0, entry.getName());
				entriesTable.setWidget(row, 1, deleteButton);
				
				row++;
			}
		}
	}
}
