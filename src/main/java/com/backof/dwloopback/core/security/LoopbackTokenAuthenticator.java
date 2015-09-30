package com.backof.dwloopback.core.security;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.backof.dwloopback.core.mongo.Collections;
import com.google.common.base.Optional;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoopbackTokenAuthenticator implements Authenticator<AccessToken, User> {

	private final MongoClient mongoClient;
	private static final Logger log = LoggerFactory.getLogger(LoopbackTokenAuthenticator.class);
	public LoopbackTokenAuthenticator(MongoClient client) {
		this.mongoClient = client;
	}

	@Override
	public Optional<User> authenticate(AccessToken token) throws AuthenticationException {

		try {
			User user = getUserFromAuthToken(token);
			log.info("authenticated: " + user.toString());
			return Optional.of(getUserFromAuthToken(token));
		} catch(Exception ex) {
			log.warn("Authentication Error. [token:" + token.getAccessToken() + ", error:" + ex.getMessage() + "]");
			return Optional.absent();
		}
			
	}

	private User getUserFromAuthToken(AccessToken token) throws AuthenticationException {
		Document accessTokenDoc = mongoClient.getDatabase("thanktank").getCollection(Collections.AccessToken.getName())
				.find(eq("_id", token.getAccessToken())).first();

		if (accessTokenDoc != null) {
			User user = new User(accessTokenDoc.getString("_id"));
			user.setTokenCreateDate(accessTokenDoc.getDate("created").getTime());
			user.setTtl(accessTokenDoc.getInteger("ttl"));
			user.setUserId(accessTokenDoc.getString("userId"));
			checkTokenExpiration(user);
			Document userDoc = mongoClient.getDatabase("loopback").getCollection(Collections.User.getName())
					.find(eq("_id", user.getUserId())).first();
			if (userDoc != null) {
				user.setUsername(userDoc.getString("username"));
				user.setLastName(userDoc.getString("lastName"));
				user.setFirstName(userDoc.getString("firstName"));
				user.setEmail(userDoc.getString("email"));
				// get roles
				FindIterable<Document> roleDocs = mongoClient.getDatabase("loopback")
						.getCollection(Collections.RoleMapping.getName()).find(eq("principalId", user.getUserId()));
				List<String> roles = new ArrayList<String>();
				for (Document userRole : roleDocs) {
					Document role = mongoClient.getDatabase("loopback").getCollection(Collections.Role.getName())
							.find(eq("_id", userRole.getObjectId("roleId"))).first();
					if (role != null) {
						roles.add(role.getString("name"));
					}
				}
				user.setRoles(roles);	
				return user;
			}

		}
		throw new AuthenticationException("invalid auth.");
	}

	private void checkTokenExpiration(User user) throws AuthenticationException {
		if(user.getTokenCreateDate()+user.getTtl() <= System.currentTimeMillis()) {
			Date d = new Date(user.getTokenCreateDate()+user.getTtl());
			throw new AuthenticationException("token expired " + d.toString());
		}
	}

}
