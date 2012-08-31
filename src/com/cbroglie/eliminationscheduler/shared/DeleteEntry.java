package com.cbroglie.eliminationscheduler.shared;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link DeleteEntryAction} and {@link DeleteEntryResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class DeleteEntry {
	@In(1)
	EntryDetails entry;

	@Out(1)
	boolean success;
}
