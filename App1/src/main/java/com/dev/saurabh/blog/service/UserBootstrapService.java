package com.dev.saurabh.blog.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.dev.saurabh.blog.domain.UserAccount;

@Component
@Profile(value="DEV")
public class UserBootstrapService {

	private final Logger logger = LoggerFactory.getLogger(UserBootstrapService.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserManagementService userManagementService;

	StandardPasswordEncoder encoder = new StandardPasswordEncoder();
	String samplePassword = encoder.encode("password");//"2a97516c354b68848cdbd8f54a226a0a55b21ed138e207ad6c5cbb9c00aa5aea";

	@PostConstruct
	public void init() {
		logger.debug("adding default users to database");
		
		if (!mongoTemplate.collectionExists(UserAccount.class)) {
            mongoTemplate.createCollection(UserAccount.class);
        }
		
		UserAccount user = new UserAccount("saurabh", samplePassword, "Saurabh", "Singh");
		
		user.setRole(1);//administrator
		userManagementService.create(user);

		user = new UserAccount("arun", samplePassword, "Arun", "Singh");
		user.setRole(2);
		userManagementService.create(user);

	}
}
