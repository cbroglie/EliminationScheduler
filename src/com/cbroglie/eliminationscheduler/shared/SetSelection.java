package com.cbroglie.eliminationscheduler.shared;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link SetSelectionAction} and {@link SetSelectionResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class SetSelection {
	@In(1)
	EntryDetails entry;

	@In(2)
	int week;

	@In(3)
	TeamDetails team;

	@Out(1)
	boolean success;
}
