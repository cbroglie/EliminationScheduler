package com.cbroglie.eliminationscheduler.client.event;

import com.cbroglie.eliminationscheduler.client.CurrentUser;

import com.gwtplatform.annotation.GenEvent;
import com.gwtplatform.annotation.Order;

@GenEvent
public class CurrentUserChanged {
	@Order(1)
	CurrentUser currentUser;
}
