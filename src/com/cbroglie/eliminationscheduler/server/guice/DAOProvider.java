package com.cbroglie.eliminationscheduler.server.guice;

import com.cbroglie.eliminationscheduler.shared.model.Entry;
import com.cbroglie.eliminationscheduler.shared.model.Game;
import com.cbroglie.eliminationscheduler.shared.model.Team;
import com.google.inject.Provider;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.helper.DAOBase;

public class DAOProvider implements Provider<DAOBase> {

	static {
		ObjectifyService.register(Team.class);
		ObjectifyService.register(Game.class);
		ObjectifyService.register(Entry.class);
	}

	private final DAOBase dao;

	public DAOProvider() {
		dao = new DAOBase();
	}

	@Override
	public DAOBase get() {
		return dao;
	}
}
