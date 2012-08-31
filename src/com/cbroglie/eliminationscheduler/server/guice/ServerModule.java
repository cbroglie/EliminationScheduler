package com.cbroglie.eliminationscheduler.server.guice;

import com.cbroglie.eliminationscheduler.server.CreateEntryHandler;
import com.cbroglie.eliminationscheduler.server.DeleteEntryHandler;
import com.cbroglie.eliminationscheduler.server.GetCurrentUserHandler;
import com.cbroglie.eliminationscheduler.server.GetEntriesHandler;
import com.cbroglie.eliminationscheduler.server.GetTeamScheduleHandler;
import com.cbroglie.eliminationscheduler.server.GetTeamsHandler;
import com.cbroglie.eliminationscheduler.server.GetWeeklyScheduleHandler;
import com.cbroglie.eliminationscheduler.server.GetSelectionsHandler;
import com.cbroglie.eliminationscheduler.server.SetSelectionHandler;
import com.cbroglie.eliminationscheduler.shared.CreateEntryAction;
import com.cbroglie.eliminationscheduler.shared.DeleteEntryAction;
import com.cbroglie.eliminationscheduler.shared.GetCurrentUserAction;
import com.cbroglie.eliminationscheduler.shared.GetEntriesAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetTeamsAction;
import com.cbroglie.eliminationscheduler.shared.GetWeeklyScheduleAction;
import com.cbroglie.eliminationscheduler.shared.GetSelectionsAction;
import com.cbroglie.eliminationscheduler.shared.SetSelectionAction;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(GetTeamsAction.class, GetTeamsHandler.class);
		bindHandler(GetWeeklyScheduleAction.class, GetWeeklyScheduleHandler.class);
		bindHandler(GetTeamScheduleAction.class, GetTeamScheduleHandler.class);
		bindHandler(GetSelectionsAction.class, GetSelectionsHandler.class);
		bindHandler(SetSelectionAction.class, SetSelectionHandler.class);
		bindHandler(GetCurrentUserAction.class, GetCurrentUserHandler.class);
		bindHandler(GetEntriesAction.class, GetEntriesHandler.class);
		bindHandler(CreateEntryAction.class, CreateEntryHandler.class);
		bindHandler(DeleteEntryAction.class, DeleteEntryHandler.class);
	}
}
