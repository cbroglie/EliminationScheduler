package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;


public class GameDetails implements Serializable {

	private static final long serialVersionUID = 8078997378347011573L;

	private Long id;
	private int week;
	private TeamDetails homeTeam;
	private TeamDetails awayTeam;

	public GameDetails() {
		this.id = new Long(0);
		this.week = 0;
		this.homeTeam = new TeamDetails(new Long(0), "HOME");
		this.awayTeam = new TeamDetails(new Long(0), "AWAY");
	}

	public GameDetails(Long id, int week, TeamDetails homeTeam, TeamDetails awayTeam) {
		this.id = id;
		this.week = week;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	public Long getId() {
		return id;
	}

	public TeamDetails getHomeTeam() {
		return homeTeam;
	}
	
	public TeamDetails getAwayTeam() {
		return awayTeam;
	}

	public int getWeek() {
		return week;
	}
}
