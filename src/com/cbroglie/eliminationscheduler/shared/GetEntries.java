package com.cbroglie.eliminationscheduler.shared;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.shared.model.EntryDetails;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link GetEntriesAction} and {@link GetEntriesResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class GetEntries {
	@In(1)
	String userId;

	@Out(1)
	ArrayList<EntryDetails> entries;
}
