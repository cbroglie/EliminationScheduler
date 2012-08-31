package com.cbroglie.eliminationscheduler.shared;

public final class NFLConstants {

	public static final int NUM_TEAMS = 32;

	public static final int WEEKS_PER_SEASON = 17;
	
	public static final int BYE_WEEKS_PER_TEAM = 1;
	
	public static final int GAMES_PER_SEASON = ((NUM_TEAMS / 2) * (WEEKS_PER_SEASON - BYE_WEEKS_PER_TEAM));

	public static final int MAX_GAMES_PER_WEEK = (NUM_TEAMS / 2);
}
