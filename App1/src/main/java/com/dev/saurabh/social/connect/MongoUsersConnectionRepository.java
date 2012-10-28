package com.dev.saurabh.social.connect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

//sadly there is no default implementation for Mongo :(
public class MongoUsersConnectionRepository implements UsersConnectionRepository {
	
	private String myUserId;
	
	@Autowired
	private ConnectionService mongoConnectionService;
	private ConnectionFactoryLocator myConnectionFactoryLocator;
	private TextEncryptor myTextEncryptor;
	private ConnectionSignUp myConnectionSignUp;
	
	@Autowired
	private MongoTemplate mongotemplate;

	@Autowired
	public MongoUsersConnectionRepository(String userId, 
		ConnectionService connectionService, 
		ConnectionFactoryLocator connectionFactoryLocator,
		TextEncryptor textEncryptor) {

		myUserId = userId;
		mongoConnectionService = connectionService;
		myConnectionFactoryLocator = connectionFactoryLocator;
		myTextEncryptor = textEncryptor;
	}
	
	public MongoUsersConnectionRepository(MongoTemplate mongoTemplate,
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor noOpText) {
		
		myConnectionFactoryLocator = connectionFactoryLocator;
		myTextEncryptor = noOpText;
	}

	public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
		myConnectionSignUp = connectionSignUp;
	}

	public void removeConnections(String providerId) {
		mongoConnectionService.remove(myUserId, providerId);
	}

	
	public void removeConnection(ConnectionKey connectionKey) {
		mongoConnectionService.remove(myUserId, connectionKey);
	}

	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		String providerId = connection.getKey().getProviderId();
		List<String> result = mongoConnectionService.getUserIds(providerId, connection.getKey().getProviderUserId());
		
		if(result == null || result.size() == 0)
		{
			mongoConnectionService.create(connection.getKey().getProviderUserId(), connection, 1);
			result = new ArrayList<String>();
			result.add(connection.getKey().getProviderUserId());
		}
		
		return result;
	}

	public Set<String> findUserIdsConnectedTo(String providerId,
			Set<String> providerUserIds) {
		return mongoConnectionService.getUserIds(providerId, providerUserIds);
	}

	public ConnectionRepository createConnectionRepository(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId cannot be null");
		}
		return new MongoConnectionRepository(userId, mongoConnectionService, myConnectionFactoryLocator, myTextEncryptor);
	}

}
