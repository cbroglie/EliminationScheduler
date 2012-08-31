package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Unindexed;

@Cached
@Unindexed
public class Team implements Serializable {

	private static final long serialVersionUID = -5803268310289196671L;

	@Id
	Long id;

	private String displayName;
	private String fullName;

	public Team() {
		this("null", "null");
	}

	public Team(final String displayName, final String fullName) {
		this.displayName = displayName;
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getFullName() {
		return fullName;
	}
}
