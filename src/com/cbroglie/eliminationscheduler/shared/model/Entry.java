package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Id;

import com.cbroglie.eliminationscheduler.shared.NFLConstants;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

@Cached
@Unindexed
public class Entry implements Serializable {

	private static final long serialVersionUID = -6511505123105052432L;

	@Id
	private Long id;

	@Indexed
	private String userId;

	private String name;

	private ArrayList<Key<Team>> selections;

	@SuppressWarnings("unused")
	private Entry() {
		// Required for GWT serialization.
	}

	public Entry(String userId, String name) {
		this.userId = userId;
		this.name = name;
		this.selections = new ArrayList<Key<Team>>(NFLConstants.WEEKS_PER_SEASON);
		for (int week = 0; week < NFLConstants.WEEKS_PER_SEASON; week++) {
			this.selections.add(null);
		}
	}

	public Long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public void setSelection(int week, Key<Team> team) {
		this.selections.set(week, team);
	}

	public Key<Team> getSelection(int week) {
		return this.selections.get(week);
	}
}
