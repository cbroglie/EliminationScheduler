package com.cbroglie.eliminationscheduler.client.event;

import com.gwtplatform.annotation.GenEvent;
import com.gwtplatform.annotation.Order;

@GenEvent
public class Loading {
	@Order(1)
	boolean finished;
}
