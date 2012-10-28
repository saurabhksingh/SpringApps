package com.dev.saurabh.social.connect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

import com.dev.saurabh.blog.domain.UserAccount;
import com.mongodb.WriteConcern;

public class MongoConnectionService implements ConnectionService{
	
	private final MongoTemplate myMongoTemplate ;
	private final ConnectionConverter myConnectionConverter;

	@Autowired
	public MongoConnectionService(MongoTemplate mongoTemplate, ConnectionConverter converter) {
		myMongoTemplate = mongoTemplate;
		myConnectionConverter = converter;
	}

	public int getMaxRank(String userId, String providerId) {
		Query q = new Query(Criteria.where("userId").is(userId).and("providerId").is(providerId));
		q.sort().on("rank", Order.DESCENDING);
		UserAccount cnn = myMongoTemplate.findOne(q, UserAccount.class);

		if (cnn==null)
			return 1;

		return cnn.getRank() + 1;
	}

	public void create(String userId, Connection<?> userConn, int rank) {
		UserAccount mongoCnn = myConnectionConverter.convert(userConn);
		mongoCnn.setUserId(userId);
		mongoCnn.setRank(rank);
		myMongoTemplate.insert(mongoCnn);
	}

	public void update(String userId, Connection<?> userConn) {
		UserAccount mongoCnn = myConnectionConverter.convert(userConn);
		mongoCnn.setUserId(userId);
		try {
			myMongoTemplate.setWriteConcern(WriteConcern.SAFE);
			myMongoTemplate.save(mongoCnn); 
		} catch (DuplicateKeyException e) {
			Query q = new Query(Criteria.where("userId").is(userId).and("providerId").is(mongoCnn.getProviderId())
					.and("providerUserId").is(mongoCnn.getProviderUserId()));

			Update update = Update.update("expireTime", mongoCnn.getExpireTime())
					.set("accessToken", mongoCnn.getAccessToken())
					.set("profileUrl", mongoCnn.getProfileUrl())
					.set("imageUrl", mongoCnn.getImageUrl())
					.set("displayName", mongoCnn.getDisplayName());

			myMongoTemplate.findAndModify(q, update, UserAccount.class);
		}
	}

	public void remove(String userId, ConnectionKey connectionKey) {
		Query q = new Query(Criteria.where("userId").is(userId)
				.and("providerId").is(connectionKey.getProviderId())
				.and("providerUserId").is(connectionKey.getProviderUserId()));
		myMongoTemplate.remove(q, UserAccount.class);		
	}

	public void remove(String userId, String providerId) {
		Query q = new Query(Criteria.where("userId").is(userId)
				.and("providerId").is(providerId));

		myMongoTemplate.remove(q, UserAccount.class);
	}

	public Connection<?> getPrimaryConnection(String userId, String providerId) {
		Query q = new Query(Criteria.where("userId").is(userId).
				and("providerId").is(providerId).
				and("rank").is(1));

		UserAccount mc = myMongoTemplate.findOne(q, UserAccount.class);
		return myConnectionConverter.convert(mc);
	}

	
	public Connection<?> getConnection(String userId, String providerId, String providerUserId) {
		Query q = new Query(Criteria.where("userId").is(userId)
				.and("providerId").is(providerId)
				.and("providerUserId").is(providerUserId));

		UserAccount mc = myMongoTemplate.findOne(q, UserAccount.class);
		return myConnectionConverter.convert(mc);
	}

	public List<Connection<?>> getConnections(String userId) {
		// select where userId = ? order by providerId, rank
		Query q = new Query(Criteria.where("userId").is(userId));
		q.sort().on("providerId", Order.ASCENDING).on("rank", Order.ASCENDING);

		return runQuery(q);
	}


	public List<Connection<?>> getConnections(String userId, String providerId) {
		Query q = new Query(Criteria.where("userId").is(userId).and("providerId").is(providerId));
		q.sort().on("rank", Order.ASCENDING);

		return runQuery(q);
	}

	
	public List<Connection<?>> getConnections(String userId, MultiValueMap<String, String> providerUsers) {

		if (providerUsers == null || providerUsers.isEmpty()) {
			throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
		}

		List<Criteria> lc = new ArrayList<Criteria>();
		for (Entry<String, List<String>> entry : providerUsers.entrySet()) {
			String providerId = entry.getKey();

			lc.add(Criteria.where("providerId").is(providerId)
				.and("providerUserId").in(entry.getValue()));
		}

		Criteria criteria = Criteria.where("userId").is(userId);
		criteria.orOperator(lc.toArray(new Criteria[lc.size()]));

		Query q = new Query(criteria);
		q.sort().on("providerId", Order.ASCENDING).on("rank", Order.ASCENDING);

		return runQuery(q);
	}

	
	public Set<String> getUserIds(String providerId, Set<String> providerUserIds) {
		Query q = new Query(Criteria.where("providerId").is(providerId)
				.and("providerUserId").in(new ArrayList<String>(providerUserIds)));
		q.fields().include("userId");

		List<UserAccount> results = myMongoTemplate.find(q, UserAccount.class);
		Set<String> userIds = new HashSet<String>();
		for (UserAccount mc : results) {
			userIds.add(mc.getUserId());
		}

		return userIds;
	}

	
	public List<String> getUserIds(String providerId, String providerUserId) {
		
		Query q = new Query(Criteria.where("providerId").is(providerId)
				.and("providerUserId").is(providerUserId));
		q.fields().include("userId");

		List<UserAccount> results = myMongoTemplate.find(q, UserAccount.class);
		List<String> userIds = new ArrayList<String>();
		for (UserAccount mc : results) {
			userIds.add(mc.getUserId());
		}

		return userIds;
	}
	
	private List<Connection<?>> runQuery(Query query) {
		List<UserAccount> results = myMongoTemplate.find(query, UserAccount.class);
		List<Connection<?>> l = new ArrayList<Connection<?>>();
		for (UserAccount mc : results) {
			l.add(myConnectionConverter.convert(mc));
		}

		return l;
	}

}
