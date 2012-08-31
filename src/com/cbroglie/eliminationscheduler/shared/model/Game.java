package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

@Cached
@Unindexed
public class Game implements Serializable {

	private static final long serialVersionUID = 656497262668613350L;

	@Id
	Long id;

	@Indexed
	private int week;

	private Date date;

	@Indexed
	private Key<Team> homeTeam;

	@Indexed
	private Key<Team> awayTeam;

	public Game() {
		this(0, null, null, null);
	}

	public Game(int week, Date date, Key<Team> homeTeam, Key<Team> awayTeam) {
		this.week = week;
		this.date = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	public Long getId() {
		return id;
	}

	public int getWeek() {
		return week;
	}

	public Date getDate() {
		return date;
	}

	public Key<Team> getHomeTeam() {
		return homeTeam;
	}

	public Key<Team> getAwayTeam() {
		return awayTeam;
	}
}
