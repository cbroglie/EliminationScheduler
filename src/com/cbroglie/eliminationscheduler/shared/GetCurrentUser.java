package com.cbroglie.eliminationscheduler.shared;

import com.cbroglie.eliminationscheduler.shared.model.UserDetails;
import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCurrentUser {
	@In(1)
	String requestUri;

	@Out(1)
	UserDetails user;
}
