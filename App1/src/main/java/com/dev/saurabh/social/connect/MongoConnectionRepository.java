package com.dev.saurabh.social.connect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public class MongoConnectionRepository implements ConnectionRepository{
	
	private final String myUserId;
	private final ConnectionService myConnectionService;
	private final ConnectionFactoryLocator myConnectionFactoryLocator;
	private final TextEncryptor myTextEncryptor;
	
	public MongoConnectionRepository(String userId, 
			ConnectionService connectionService, 
			ConnectionFactoryLocator connectionFactoryLocator,
			TextEncryptor textEncryptor) {
		
		myUserId = userId;
		myConnectionService = connectionService;
		myConnectionFactoryLocator = connectionFactoryLocator;
		myTextEncryptor = textEncryptor;
	}

	public MultiValueMap<String, Connection<?>> findAllConnections() {
		List<Connection<?>> resultList = myConnectionService.getConnections(myUserId);

		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		Set<String> registeredProviderIds = this.myConnectionFactoryLocator.registeredProviderIds();
		for (String registeredProviderId : registeredProviderIds) {
			connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
		}

		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			if (connections.get(providerId).size() == 0) {
				connections.put(providerId, new LinkedList<Connection<?>>());
			}
			connections.add(providerId, connection);
		}
		return connections;
	}

	public List<Connection<?>> findConnections(String providerId) {
		return myConnectionService.getConnections(myUserId, providerId);
	}

	@SuppressWarnings("unchecked")
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		List<?> connections = findConnections(getProviderId(apiType));
		return (List<Connection<A>>) connections;
	}

	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(
			MultiValueMap<String, String> providerUserIds) {
		if (providerUserIds == null || providerUserIds.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}

		List<Connection<?>> resultList = myConnectionService.getConnections(myUserId, providerUserIds);

		MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
		for (Connection<?> connection : resultList) {
			String providerId = connection.getKey().getProviderId();
			List<String> userIds = providerUserIds.get(providerId);
			List<Connection<?>> connections = connectionsForUsers.get(providerId);
			if (connections == null) {
				connections = new ArrayList<Connection<?>>(userIds.size());
				for (int i = 0; i < userIds.size(); i++) {
					connections.add(null);
				}
				connectionsForUsers.put(providerId, connections);
			}
			String providerUserId = connection.getKey().getProviderUserId();
			int connectionIndex = userIds.indexOf(providerUserId);
			connections.set(connectionIndex, connection);
		}
		return connectionsForUsers;
	}

	public Connection<?> getConnection(ConnectionKey connectionKey) {
		try {
			return myConnectionService.getConnection(myUserId, 
				connectionKey.getProviderId(), 
				connectionKey.getProviderUserId());
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchConnectionException(connectionKey);
		}
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> getConnection(Class<A> apiType,
			String providerUserId) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		@SuppressWarnings("unchecked")
		Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
		if (connection == null) {
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@SuppressWarnings("unchecked")
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = getProviderId(apiType);
		return (Connection<A>) findPrimaryConnection(providerId);
	}

	public void addConnection(Connection<?> connection) {
		myConnectionService.create(myUserId, connection, 1);
		
	}

	public void updateConnection(Connection<?> connection) {
		
		myConnectionService.update(myUserId, connection);
		
	}

	public void removeConnections(String providerId) {
		
		myConnectionService.remove(myUserId, providerId);
	}

	public void removeConnection(ConnectionKey connectionKey) {
		
		myConnectionService.remove(myUserId, connectionKey);
		
	}
	
	private <A> String getProviderId(Class<A> apiType) {
		
		return myConnectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}

	private Connection<?> findPrimaryConnection(String providerId) {
		
		return myConnectionService.getPrimaryConnection(myUserId, providerId);
	}
}