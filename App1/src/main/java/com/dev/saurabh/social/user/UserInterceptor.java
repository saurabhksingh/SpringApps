package com.dev.saurabh.social.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import com.dev.saurabh.blog.domain.UserAccount;

public class UserInterceptor extends HandlerInterceptorAdapter {

	private final UsersConnectionRepository myConnectionRepository;
	
	private final UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

	public UserInterceptor(UsersConnectionRepository connectionRepository) {
		myConnectionRepository = connectionRepository;
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		rememberUser(request, response);
		handleSignOut(request, response);			
		if (SocialSecurityContext.userSignedIn() || requestForSignIn(request)) {
			return true;
		} else {
			return requireSignIn(request, response);
		}
	}
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		SocialSecurityContext.remove();
	}
	
	private void rememberUser(HttpServletRequest request, HttpServletResponse response) {
		String userId = userCookieGenerator.readCookieValue(request);
		if (userId == null) {
			return;
		}
		if (!userNotFound(userId)) {
			userCookieGenerator.removeCookie(response);
			return;
		}
		UserAccount user = new UserAccount();
		user.setUserId(userId);
		SocialSecurityContext.setCurrentUser(user);
	}

	private void handleSignOut(HttpServletRequest request, HttpServletResponse response) {
		if (SocialSecurityContext.userSignedIn() && request.getServletPath().startsWith("/logout")) {
			myConnectionRepository.createConnectionRepository(SocialSecurityContext.getCurrentUser().getUserId()).removeConnections("facebook");
			userCookieGenerator.removeCookie(response);
			SocialSecurityContext.remove();			
		}
	}
		
	private boolean requestForSignIn(HttpServletRequest request) {
		return request.getServletPath().startsWith("/login");
	}
	
	private boolean requireSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		new RedirectView("/login", true).render(null, request, response);
		return false;
	}

	private boolean userNotFound(String userId) {
		return myConnectionRepository.createConnectionRepository(userId).findPrimaryConnection(Facebook.class) != null;
	}
	
}