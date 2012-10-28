package com.dev.saurabh.social.connect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.dev.saurabh.blog.domain.UserAccount;

@Component
public class ConnectionConverter {
	private final ConnectionFactoryLocator myConnectionFactoryLocator;
	private final TextEncryptor myTextEncryptor;

	@Autowired
	public ConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator,
		TextEncryptor textEncryptor) {

		myConnectionFactoryLocator = connectionFactoryLocator;
		myTextEncryptor = textEncryptor;
	}

	public Connection<?> convert(UserAccount cnn) {
		if (cnn==null) return null;

		ConnectionData connectionData = fillConnectionData(cnn);
		ConnectionFactory<?> connectionFactory = myConnectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	private ConnectionData fillConnectionData(UserAccount uc) {
		return new ConnectionData(uc.getProviderId(),
			uc.getProviderUserId(),
			uc.getDisplayName(),
			uc.getProfileUrl(),
			uc.getImageUrl(),
			decrypt(uc.getAccessToken()),
			decrypt(uc.getSecret()),
			decrypt(uc.getRefreshToken()),
			uc.getExpireTime());
	}

	public UserAccount convert(Connection<?> cnn) {
		ConnectionData data = cnn.createData();

		UserAccount userConn = new UserAccount();
		userConn.setRole(1);
		userConn.setProviderId(data.getProviderId());
		userConn.setProviderUserId(data.getProviderUserId());
		userConn.setDisplayName(data.getDisplayName());
		userConn.setProfileUrl(data.getProfileUrl());
		userConn.setImageUrl(data.getImageUrl());
		userConn.setAccessToken(encrypt(data.getAccessToken()));
		userConn.setSecret(encrypt(data.getSecret()));
		userConn.setRefreshToken(encrypt(data.getRefreshToken()));
		userConn.setExpireTime(data.getExpireTime());
		return userConn;
	}


	private String decrypt(String encryptedText) {
		return encryptedText != null ? myTextEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private String encrypt(String text) {
		return text != null ? myTextEncryptor.encrypt(text) : text;
	}
}