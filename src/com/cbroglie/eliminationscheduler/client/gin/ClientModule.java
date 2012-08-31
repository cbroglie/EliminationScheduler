package com.cbroglie.eliminationscheduler.client.gin;

import com.cbroglie.eliminationscheduler.client.AdminGatekeeper;
import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.EliminationSchedulerPlaceManager;
import com.cbroglie.eliminationscheduler.client.LoginGatekeeper;
import com.cbroglie.eliminationscheduler.client.NameTokens;
import com.cbroglie.eliminationscheduler.client.presenter.AboutUsPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.ErrorPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.LoadingPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.LoginPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.MainPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.ManageEntriesPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.MyPicksPresenter;
import com.cbroglie.eliminationscheduler.client.presenter.SchedulePresenter;
import com.cbroglie.eliminationscheduler.client.presenter.UploadSchedulePresenter;
import com.cbroglie.eliminationscheduler.client.resources.Resources;
import com.cbroglie.eliminationscheduler.client.view.AboutUsView;
import com.cbroglie.eliminationscheduler.client.view.ErrorView;
import com.cbroglie.eliminationscheduler.client.view.LoadingView;
import com.cbroglie.eliminationscheduler.client.view.LoginView;
import com.cbroglie.eliminationscheduler.client.view.MainView;
import com.cbroglie.eliminationscheduler.client.view.ManageEntriesView;
import com.cbroglie.eliminationscheduler.client.view.MyPicksView;
import com.cbroglie.eliminationscheduler.client.view.ScheduleView;
import com.cbroglie.eliminationscheduler.client.view.UploadScheduleView;
import com.cbroglie.eliminationscheduler.shared.Constants;

import com.google.inject.Singleton;

import com.gwtplatform.dispatch.shared.SecurityCookie;
import com.gwtplatform.mvp.client.DefaultEventBus;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		// Constants
	    bindConstant().annotatedWith(SecurityCookie.class).to(Constants.securityCookieName);
		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.homePage);
		bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.errorPage);

		// Singletons
		bind(Resources.class).in(Singleton.class);
		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(EliminationSchedulerPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);
		bind(CurrentUser.class).asEagerSingleton();
	    bind(LoginGatekeeper.class).in(Singleton.class);
	    bind(AdminGatekeeper.class).in(Singleton.class); 

		// Presenter widgets
		bindSingletonPresenterWidget(LoginPresenter.class, LoginPresenter.MyView.class, LoginView.class);
		bindSingletonPresenterWidget(LoadingPresenter.class, LoadingPresenter.MyView.class, LoadingView.class);

		// Presenters
		bindPresenter(AboutUsPresenter.class, AboutUsPresenter.MyView.class, AboutUsView.class, AboutUsPresenter.MyProxy.class);
		bindPresenter(ErrorPresenter.class, ErrorPresenter.MyView.class, ErrorView.class, ErrorPresenter.MyProxy.class);
		bindPresenter(MainPresenter.class, MainPresenter.MyView.class, MainView.class, MainPresenter.MyProxy.class);
		bindPresenter(ManageEntriesPresenter.class, ManageEntriesPresenter.MyView.class, ManageEntriesView.class, ManageEntriesPresenter.MyProxy.class);
		bindPresenter(MyPicksPresenter.class, MyPicksPresenter.MyView.class, MyPicksView.class, MyPicksPresenter.MyProxy.class);
		bindPresenter(SchedulePresenter.class, SchedulePresenter.MyView.class, ScheduleView.class, SchedulePresenter.MyProxy.class);
		bindPresenter(UploadSchedulePresenter.class, UploadSchedulePresenter.MyView.class, UploadScheduleView.class, UploadSchedulePresenter.MyProxy.class);
	}
}
