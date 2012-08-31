package com.cbroglie.eliminationscheduler.server.guice;

import com.cbroglie.eliminationscheduler.server.ScheduleBuilder;
import com.cbroglie.eliminationscheduler.server.ScheduleUploadServlet;
import com.cbroglie.eliminationscheduler.shared.Constants;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;

import com.gwtplatform.dispatch.server.DispatchServiceImpl;
import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.SecurityCookie;

public class DispatchServletModule extends ServletModule {

	@Override
	public void configureServlets() {
		bindConstant().annotatedWith(SecurityCookie.class).to(Constants.securityCookieName);

		bind(ScheduleBuilder.class).in(Singleton.class);
		bind(ScheduleUploadServlet.class).in(Singleton.class);

		serve("*.gupld").with(ScheduleUploadServlet.class);
		serve("/eliminationscheduler/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);
	}
}
