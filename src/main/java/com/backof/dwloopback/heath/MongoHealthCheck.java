package com.backof.dwloopback.heath;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;

public class MongoHealthCheck extends HealthCheck {
	 
    private MongoClient mongo;
 
    public MongoHealthCheck(MongoClient mongo) {
        super();
        this.mongo = mongo;
    }
 
    @SuppressWarnings("deprecation")
	@Override
    protected Result check() throws Exception {
        mongo.getDatabaseNames();
        return Result.healthy();
    }
 
}