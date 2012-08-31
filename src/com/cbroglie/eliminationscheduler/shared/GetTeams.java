package com.cbroglie.eliminationscheduler.shared;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;
import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link GetTeamsAction} and {@link GetTeamsResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class GetTeams {

	@Out(1)
	ArrayList<TeamDetails> teams;
}
