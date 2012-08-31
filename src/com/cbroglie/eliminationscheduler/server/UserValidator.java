package com.cbroglie.eliminationscheduler.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserValidator {

	public static boolean isCurrentUser(String userId) {
		if (userId == null) {
			return false;
		}

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			return false;
		}
		else if (!user.getUserId().equals(userId)) {
			return false;
		}

		return true;
	}
}
