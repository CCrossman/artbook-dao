package com.artbook.dao.integration.base;

/**
 * <b>Test Scope Authentication</b><br>
 * Because of the custom APIs require a secure user, Here we use basic
 * authentication.<br>
 * 
 * Some user and resource is already import by src/main/resources/imports.sql
 * <br>
 * If you do not want to use it, and only want to use test scope import, You can
 * copy the imports.sql to src/text/resources
 * 
 * @author Wenbo Wang (jackie-1685@163.com)
 */
public class TestScopeAuthentication {

	public static final String USER_NAME = "user";
	public static final String USER_PASSWORD = "user";

	public static final String ADMIN_NAME = "admin";
	public static final String ADMIN_PASSWORD = "admin";
}
