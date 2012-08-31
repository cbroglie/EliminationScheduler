package com.cbroglie.eliminationscheduler.client.view;

import java.util.ArrayList;
import java.util.List;

import com.cbroglie.eliminationscheduler.client.presenter.MyPicksPresenter;
import com.cbroglie.eliminationscheduler.client.ui.SelectOneListBox;
import com.cbroglie.eliminationscheduler.shared.NFLConstants;
import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class MyPicksView extends CompositeViewWithUiHandlers<MyPicksUiHandlers> implements MyPicksPresenter.MyView {

	private static MyPicksViewUiBinder uiBinder = GWT.create(MyPicksViewUiBinder.class);

	interface MyPicksViewUiBinder extends UiBinder<Widget, MyPicksView> {}

	interface Style extends CssResource {
		String selections();
	}

	@UiField
	Style style;

	@UiField(provided = true)
	final SelectOneListBox<EntryDetails> entriesListBox;
	
	@UiField
	FlexTable selectionsTable;

	public MyPicksView() {
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
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void refreshEntries() {
		if (getUiHandlers() == null) {
			refreshEntriesItems(null);
		}
		else {
			refreshEntriesItems(getUiHandlers().getEntries());
		}
	}

	private void refreshEntriesItems(List<EntryDetails> entries) {
		ArrayList<EntryDetails> items = new ArrayList<EntryDetails>();

		// Insert a null team at index 0 to show "select an entry" 
		items.add(0, null);

		if (entries != null) {
			for (EntryDetails entry : entries) {
				items.add(entry);		
			}
		}

		this.entriesListBox.setItems(items);
	}

	@Override
	public void refreshSelections() {
		if (getUiHandlers() != null) {
			selectionsTable.removeAllRows();
			
			List<TeamDetails> selections = getUiHandlers().getSelections();
			if (selections != null) {
				for (int weekIdx = 0; weekIdx < NFLConstants.WEEKS_PER_SEASON; weekIdx++) {
					TeamDetails team = selections.get(weekIdx);

					String text = (team != null ? team.getDisplayName() : "--");

					setSelectionsTableCell(weekIdx, 0, Integer.toString(weekIdx + 1));
					setSelectionsTableCell(weekIdx, 1, text);
				}
			}
		}
	}

	private void setSelectionsTableCell(int row, int col, String text) {
		selectionsTable.setText(row, col, text);
		selectionsTable.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		selectionsTable.getCellFormatter().addStyleName(row, col, style.selections());
	}
}
