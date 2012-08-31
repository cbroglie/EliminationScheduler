package com.cbroglie.eliminationscheduler.server;

import java.io.Serializable;

public class InvalidScheduleFileException extends Exception implements Serializable {

	private static final long serialVersionUID = -5941382797752884083L;

	public InvalidScheduleFileException() {
		super();
	}

	public InvalidScheduleFileException(String message) {
		super(message);
	}
}
