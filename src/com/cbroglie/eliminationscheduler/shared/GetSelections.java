package com.cbroglie.eliminationscheduler.shared;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link GetSelectionsAction} and {@link GetSelectionsResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class GetSelections {
	@In(1)
	EntryDetails entry;

	@Out(1)
	ArrayList<TeamDetails> selections;
}
