package com.dev.saurabh.social.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.CookieGenerator;


public class UserCookieGenerator {

	private final CookieGenerator myUserCookieGenerator = new CookieGenerator();

	public UserCookieGenerator() {
		myUserCookieGenerator.setCookieName("sample_app_user_tracker");
	}

	public void addCookie(String userId, HttpServletResponse response) {
		myUserCookieGenerator.addCookie(response, userId);
	}
	
	public void removeCookie(HttpServletResponse response) {
		myUserCookieGenerator.addCookie(response, "");
	}
	
	public String readCookieValue(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(myUserCookieGenerator.getCookieName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

}