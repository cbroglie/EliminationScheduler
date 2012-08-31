package com.cbroglie.eliminationscheduler.shared.model;

import java.io.Serializable;

public class TeamDetails implements Serializable {

	private static final long serialVersionUID = -4751543678650442520L;

	private Long id;
	private String displayName;

	public TeamDetails() {
		this(new Long(0), "null");
	}

	public TeamDetails(Long id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public Long getId() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public boolean equals(Object rhs) {
		if (this == rhs) {
			return true;
		}

		if (!(rhs instanceof TeamDetails)) {
			return false;
		}

		return this.id.equals(((TeamDetails)rhs).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
