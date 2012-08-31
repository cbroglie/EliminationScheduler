package com.cbroglie.eliminationscheduler.shared;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.cbroglie.eliminationscheduler.shared.model.TeamDetails;
import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link GetTeamScheduleAction} and {@link GetTeamScheduleResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class GetTeamSchedule {

	@In(1)
	TeamDetails team;

	@Out(1)
	ArrayList<GameDetails> games;
}
