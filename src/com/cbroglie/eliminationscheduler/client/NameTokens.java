package com.cbroglie.eliminationscheduler.client;

/**
 * The central location of all name tokens for the application. All
 * {@link ProxyPlace} classes get their tokens from here. This class also makes
 * it easy to use name tokens as a resource within UIBinder xml files.
 * <p />
 * The public static final String is used within the annotation
 * {@link NameToken}, which can't use a method and the method associated with
 * this field is used within UiBinder which can't access static fields.
 * <p />
 * Also note the exclamation mark in front of the tokens, this is used for
 * search engine crawling support.
 *
 * @author cbroglie
 */
public class NameTokens {

	public static final String schedulePage = "!schedule";
	public static String getSchedulePage() {
		return schedulePage;
	}

	public static final String homePage = schedulePage; // The schedule page is used as the home page
	public static String getHomePage() {
		return homePage;
	}

	public static final String loginPage = "!login";
	public static String getLoginPage() {
		return loginPage;
	}

	public static final String manageEntriesPage = "!manage";
	public static String getManageEntriesPage() {
		return manageEntriesPage;
	}

	public static final String myPicksPage = "!myPicks";
	public static String getMyPicksPage() {
		return myPicksPage;
	}

	public static final String aboutUsPage = "!aboutUs";
	public static String getAboutUsPage() {
		return aboutUsPage;
	}

	public static final String errorPage = "!error";
	public static String getErrorPage() {
		return errorPage;
	}

	public static final String uploadSchedulePage = "!upload";
	public static String getUploadSchedulePage() {
		return uploadSchedulePage;
	}
}
