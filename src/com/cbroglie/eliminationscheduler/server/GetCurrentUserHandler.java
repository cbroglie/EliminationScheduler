package com.cbroglie.eliminationscheduler.server;

import com.cbroglie.eliminationscheduler.shared.GetCurrentUserAction;
import com.cbroglie.eliminationscheduler.shared.GetCurrentUserResult;
import com.cbroglie.eliminationscheduler.shared.model.UserDetails;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetCurrentUserHandler implements ActionHandler<GetCurrentUserAction, GetCurrentUserResult> {

	@Override
	public GetCurrentUserResult execute(GetCurrentUserAction action, ExecutionContext context) throws ActionException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		UserDetails userDetails = new UserDetails();
		String requestUri = action.getRequestUri();

		if (user != null) {
			userDetails.setLoggedIn(true);
			userDetails.setEmail(user.getEmail());
			userDetails.setNickname(user.getNickname());
			userDetails.setUserId(user.getUserId());
			userDetails.setAdministrator(userService.isUserAdmin());
			userDetails.setLogoutUrl(userService.createLogoutURL(requestUri));
		}
		else {
			userDetails.setLoggedIn(false);
			userDetails.setLoginUrl(userService.createLoginURL(requestUri));
		}

		return new GetCurrentUserResult(userDetails);
	}

	@Override
	public Class<GetCurrentUserAction> getActionType() {
		return GetCurrentUserAction.class;
	}

	@Override
	public void undo(GetCurrentUserAction action, GetCurrentUserResult result, ExecutionContext context) throws ActionException {
		// Pure get, nothing to undo.
	}
}
