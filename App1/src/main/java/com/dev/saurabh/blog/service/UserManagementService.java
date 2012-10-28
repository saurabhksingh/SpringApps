package com.dev.saurabh.blog.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.saurabh.blog.domain.UserAccount;

@Service("userMangementService")
@Transactional
public class UserManagementService implements UserDetailsService {

	
	private String USER_ROLE = "ROLE_USER";
	private String ADMIN_ROLE = "ROLE_ADMIN";
	private String USER_ID = "userId";

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		logger.info("Inside loadUserByUsername");
		UserAccount user = getUserDetail(userId);
		if(user == null)
		{
			return null;
		}
		User userDetail = new User(user.getUserId(), user.getPassword(), true,
				true, true, true, getGrantedAuthorities(user.getRole()));

		return userDetail;

	}

	public List<GrantedAuthority> getGrantedAuthorities(Integer role) {
		logger.info("Inside getGrantedAuthorities");
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		switch(role.intValue())
		{
			case 1:
				authList.add(new SimpleGrantedAuthority(USER_ROLE));
				authList.add(new SimpleGrantedAuthority(ADMIN_ROLE));
				break;
				
			case 2:
				authList.add(new SimpleGrantedAuthority(USER_ROLE));
				break;
			
			default:
				break;
		}
		
		return authList;
	}

	public UserAccount getUserDetail(String userId) {
		logger.info("Inside getGrantedAuthorities");
		Query query = new Query(Criteria.where(USER_ID).is(userId));
		UserAccount user = mongoTemplate.findOne(query, UserAccount.class);

		return user;
	}

	public boolean create(UserAccount user) {
		logger.info("Create a new user account");
		if (!mongoTemplate.collectionExists(UserAccount.class)) {
            mongoTemplate.createCollection(UserAccount.class);
        }
		if (loadUserByUsername(user.getUserId()) != null) {
			return false;
		}
		user.setDeleted(false);
		mongoTemplate.insert(user, mongoTemplate.getCollectionName(UserAccount.class));
		
		return true;
		
	}

}
