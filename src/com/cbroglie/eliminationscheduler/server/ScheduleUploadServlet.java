package com.cbroglie.eliminationscheduler.server;

import java.util.List;

import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.AppEngineUploadAction;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ScheduleUploadServlet extends AppEngineUploadAction {

	private static final long serialVersionUID = 788860824312745218L;

	final ScheduleBuilder scheduleBuilder;

	@Inject
	public ScheduleUploadServlet(final ScheduleBuilder scheduleBuilder) {
		this.scheduleBuilder = scheduleBuilder;
	}

	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		if (sessionFiles == null) {
			throw new UploadActionException("No files in session");
		}

		String result = null;

		try {
			scheduleBuilder.createScheduleFromFile(sessionFiles.get(0));

			result = "Schedule file uploaded successfully";
		}
		catch (InvalidScheduleFileException e) {
			throw new UploadActionException(e);
		}
		finally {
			removeSessionFileItems(request);	
		}

		return result;
	}
}
