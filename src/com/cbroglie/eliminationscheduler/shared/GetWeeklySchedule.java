package com.cbroglie.eliminationscheduler.shared;

import java.util.ArrayList;

import com.cbroglie.eliminationscheduler.shared.model.GameDetails;
import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

/**
 * This classes uses GWTP annotation processors to generate
 * {@link GetWeeklyScheduleAction} and {@link GetWeeklyScheduleResult}.
 * 
 * @author cbroglie
 */
@GenDispatch(isSecure = false)
public class GetWeeklySchedule {

	@In(1)
	int week;
	
	@Out(1)
	ArrayList<GameDetails> games;
}
