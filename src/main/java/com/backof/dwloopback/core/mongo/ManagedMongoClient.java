package com.backof.dwloopback.core.mongo;

import com.backof.dwloopback.LoopbackAuthConfiguration;
import com.mongodb.MongoClient;

import io.dropwizard.lifecycle.Managed;

public class ManagedMongoClient implements Managed {

	private MongoClient mongo;

	public ManagedMongoClient(LoopbackAuthConfiguration configuration) {
		this.mongo = new MongoClient(configuration.mongohost, configuration.mongoport);
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void stop() throws Exception {
		mongo.close();
	}

	public MongoClient getClient() {
		return this.mongo;
	}
}