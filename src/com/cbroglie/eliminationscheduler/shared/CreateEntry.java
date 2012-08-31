package com.cbroglie.eliminationscheduler.shared;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link CreateEntryAction} and {@link CreateEntryResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class CreateEntry {
	@In(1)
	String userId;

	@In(2)
	String name;

	@Out(1)
	boolean success;
}
