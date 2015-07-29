package com.backof.dwloopback.heath;

import com.codahale.metrics.health.HealthCheck;

public class ServiceHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		return Result.healthy();
	}

}