package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;

public class UserDetails implements Serializable {

	private static final long serialVersionUID = -1906119203787336697L;

	private String userId;
	private String email;
	private String nickname;
	private String loginUrl;
	private String logoutUrl;
	private boolean isLoggedIn;
	private boolean isAdministrator;

	public UserDetails() {
		this.isLoggedIn = false;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setAdministrator(boolean isAdministrator) {
		this.isAdministrator = isAdministrator;
	}

	public boolean isAdministrator() {
		return isAdministrator;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	@Override
	public boolean equals(Object rhs) {
		if (this == rhs) {
			return true;
		}

		if (!(rhs instanceof UserDetails)) {
			return false;
		}

		return this.userId.equals(((UserDetails)rhs).userId);
	}

	@Override
	public int hashCode() {
		return userId.hashCode();
	}
}
