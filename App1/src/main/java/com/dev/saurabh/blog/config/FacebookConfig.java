package com.dev.saurabh.blog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.dev.saurabh.blog.domain.UserAccount;
import com.dev.saurabh.blog.service.UserManagementService;
import com.dev.saurabh.social.connect.ConnectionConverter;
import com.dev.saurabh.social.connect.MongoConnectionService;
import com.dev.saurabh.social.connect.MongoUsersConnectionRepository;
import com.dev.saurabh.social.user.SignInAdapterImpl;
import com.dev.saurabh.social.user.SimpleConnectionSignUp;
import com.dev.saurabh.social.user.SocialSecurityContext;

@Configuration
public class FacebookConfig 
{
	private static final String facebookClientId="127452844071589";
	private static final String facebookClientSecret="fac966fb9598d83fac4cde57edb1225b";
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookConfig.class);
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserManagementService userManagementService;

	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionFactoryLocator connectionFactoryLocator() {
		logger.info("Initializing connectionFactoryLocator");
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(facebookClientId, facebookClientSecret) );
		
		return registry;
	}

	/**
	 * Singleton data access object providing access to connections across all users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		logger.info("Initializing usersConnectionRepository");
		MongoUsersConnectionRepository repository = new MongoUsersConnectionRepository(mongoTemplate,
				connectionFactoryLocator(), Encryptors.noOpText());
		repository.setConnectionSignUp(new SimpleConnectionSignUp());
		return repository;
	}

	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		logger.info("Initializing connectionRepository");
	    UserAccount user = SocialSecurityContext.getCurrentUser();
	    return usersConnectionRepository().createConnectionRepository(user.getUserId());
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Facebook facebook() {
		logger.info("Initializing facebook");
	    return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}
	
	@Bean
	public ProviderSignInController providerSignInController(RequestCache requestCache) {
		logger.info("Initializing ProviderSignInController");
		ConnectionFactoryLocator connFactLocatior = connectionFactoryLocator();
		UsersConnectionRepository usrConnRepo = usersConnectionRepository();
		SignInAdapterImpl signInAdapter = new SignInAdapterImpl(requestCache, userManagementService);
		ProviderSignInController controller =  new ProviderSignInController(connFactLocatior, usrConnRepo,
				signInAdapter);
		
		controller.setSignUpUrl("/signup");
		controller.setPostSignInUrl("/userhome");
		
		return controller;
	}
	
	@Bean 
	public TextEncryptor textEncryptor() {
		logger.info("Initializing textEncryptor");
		return Encryptors.noOpText();
	}
	
	@Bean
	public ConnectionConverter connectionConverter() {
		logger.info("Initializing connectionConvertor");
		return new ConnectionConverter(connectionFactoryLocator(), textEncryptor());
	}
	
	@Bean
	public MongoConnectionService mongoConnectionService() {
		logger.info("Initializing mongoConnectionService");
		return new MongoConnectionService(mongoTemplate, connectionConverter());
	}
}