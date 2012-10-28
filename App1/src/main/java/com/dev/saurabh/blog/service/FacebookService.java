package com.dev.saurabh.blog.service;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.saurabh.blog.domain.UserAccount;

@Service("facebookService")
@Transactional
public class FacebookService {

	@Autowired
	private UserManagementService userManagementService; 
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookService.class);
	
	public void postToFB(String title, String url, Principal principal)
	{
		logger.info("Inside postToFB");
		UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)principal;
		UserAccount user = (UserAccount)authToken.getPrincipal();
		String accessToken = user.getAccessToken();
		Facebook facebook = new FacebookTemplate(accessToken);
		FacebookLink link = new FacebookLink("http://google.com", 
		         "Link :  "+url, 
		        title, 
		        "Read this blog entry at blog junction");
		//facebook.feedOperations().post(user.getProviderUserId(), "test");
		facebook.feedOperations().postLink(title, link);
	}
	
}
