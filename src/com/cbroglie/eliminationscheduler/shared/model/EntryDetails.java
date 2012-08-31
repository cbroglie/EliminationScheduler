package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;

public class EntryDetails implements Serializable {

	private static final long serialVersionUID = 2794324401892423799L;

	private Long id;
	private String name;

	@SuppressWarnings("unused")
	private EntryDetails() {
		// Required for GWT serialization.
	}

	public EntryDetails(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
