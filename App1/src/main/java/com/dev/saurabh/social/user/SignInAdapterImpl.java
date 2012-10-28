package com.dev.saurabh.social.user;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

import com.dev.saurabh.blog.domain.UserAccount;
import com.dev.saurabh.blog.service.UserManagementService;

@Component
@Profile(value="DEV")
public final class SignInAdapterImpl implements SignInAdapter {

	private final RequestCache myRequestCache;
	
	private UserManagementService myUserAccountService;

	@Inject
	public SignInAdapterImpl(RequestCache requestCache, UserManagementService service) {
		myRequestCache = requestCache;
		myUserAccountService = service;
	}
	
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
		UserAccount user = myUserAccountService.getUserDetail(localUserId);
		List<GrantedAuthority> authorities = myUserAccountService.getGrantedAuthorities(user.getRole());
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
		//SignInUtils.signin(localUserId);
		return extractOriginalUrl(request);
	}

	private String extractOriginalUrl(NativeWebRequest request) {
		HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
		SavedRequest saved = myRequestCache.getRequest(nativeReq, nativeRes);
		if (saved == null) {
			return null;
		}
		myRequestCache.removeRequest(nativeReq, nativeRes);
		removeAutheticationAttributes(nativeReq.getSession(false));
		return saved.getRedirectUrl();
	}
		 
	private void removeAutheticationAttributes(HttpSession session) {
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}