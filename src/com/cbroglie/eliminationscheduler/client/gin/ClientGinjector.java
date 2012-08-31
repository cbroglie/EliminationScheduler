package com.cbroglie.eliminationscheduler.client.gin;

import com.cbroglie.eliminationscheduler.client.AdminGatekeeper;
import com.cbroglie.eliminationscheduler.client.CurrentUser;
import com.cbroglie.eliminationscheduler.client.LoginGatekeeper;
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

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;

import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface ClientGinjector extends Ginjector {
	PlaceManager getPlaceManager();
	EventBus getEventBus();
	ProxyFailureHandler getProxyFailureHandler();
	Resources getResources();

	CurrentUser getCurrentUser();

	LoginGatekeeper getLoginGatekeeper();
	AdminGatekeeper getAdminGatekeeper();

	Provider<MainPresenter> getMainPresenter();
	AsyncProvider<AboutUsPresenter> getAboutUsPresenter();
	AsyncProvider<ErrorPresenter> getErrorPresenter();
	AsyncProvider<LoadingPresenter> getLoadingPresenter();
	AsyncProvider<LoginPresenter> getLoginPresenter();
	AsyncProvider<ManageEntriesPresenter> getManageEntriesPresenter();
	AsyncProvider<MyPicksPresenter> getMyPicksPresenters();
	AsyncProvider<SchedulePresenter> getSchedulePresenter();
	AsyncProvider<UploadSchedulePresenter> getUploadSchedulePresenter();
}
