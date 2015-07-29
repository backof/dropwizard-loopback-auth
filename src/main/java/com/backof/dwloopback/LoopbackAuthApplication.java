package com.backof.dwloopback;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.File;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backof.dwloopback.core.mongo.ManagedMongoClient;
import com.backof.dwloopback.core.security.AccessToken;
import com.backof.dwloopback.core.security.LoopbackAuthFilter;
import com.backof.dwloopback.core.security.LoopbackAuthorizer;
import com.backof.dwloopback.core.security.LoopbackTokenAuthenticator;
import com.backof.dwloopback.core.security.User;
import com.backof.dwloopback.heath.MongoHealthCheck;
import com.backof.dwloopback.heath.ServiceHealthCheck;
import com.backof.dwloopback.resources.SecureResource;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class LoopbackAuthApplication extends Application<LoopbackAuthConfiguration> {

	private static Logger log = LoggerFactory.getLogger(LoopbackAuthApplication.class);
	private final static String APPNAME = "dropwizard-loopback-auth";
	private final static String HEALTHCHECK_NAME = "health";
	private final static String MONGO_HEALTHCHECK_NAME = "mongo-health";
	private final static String REALM = "backof.com";

	public static void main(final String[] args) throws Exception {
		new LoopbackAuthApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<LoopbackAuthConfiguration> bootstrap) {
	}

	@Override
	public void run(LoopbackAuthConfiguration configuration, Environment environment) throws Exception {
		log.info("-----------------starting application------------------------------");
		configureCors(environment);
		ManagedMongoClient mongo = new ManagedMongoClient(configuration);
		environment.lifecycle().manage(mongo);
		setupLogbackLogging(configuration.getLogbackXmlFile());
		runInitializeHealthcheck(configuration, environment, mongo);
		initializeProviders(configuration, environment, mongo);
		initResources(configuration, environment);
		log.info("--------------------finished start---------------------------------");
	}

	protected void configureCors(Environment environment) {
		FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
		filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, environment.getApplicationContext()
				.getContextPath() + "api/*");

		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM,
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
		filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

	}

	@Override
	public String getName() {
		return APPNAME;
	}

	private void setupLogbackLogging(String logbackFile) {
		try {
			LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
			context.reset();
			if (StringUtils.isEmpty(logbackFile)) {
				ContextInitializer initializer = new ContextInitializer(context);
				initializer.autoConfig();
			} else {
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);
				configurator.doConfigure(new File(logbackFile));
			}
		} catch (JoranException e) {
			log.error("", e);
		}
	}

	protected void runInitializeHealthcheck(LoopbackAuthConfiguration configuration, Environment environment,
			ManagedMongoClient mongo) {
		log.info("Registering Healthchecks.");
		environment.healthChecks().register(HEALTHCHECK_NAME, new ServiceHealthCheck());
		environment.healthChecks().register(MONGO_HEALTHCHECK_NAME, new MongoHealthCheck(mongo.getClient()));
	}

	protected void initializeProviders(LoopbackAuthConfiguration configuration, Environment environment,
			ManagedMongoClient mongo) {
		// Loopback Authentication
		log.info("Setting up Access Token Authentication (loopback.io)");
		CachingAuthenticator<AccessToken, User> cachingAuthenticator = new CachingAuthenticator<>(
				environment.metrics(), new LoopbackTokenAuthenticator(mongo.getClient()),
				configuration.authenticationCachePolicy);
		environment.jersey().register(
				new AuthDynamicFeature(new LoopbackAuthFilter.Builder<User>().setAuthenticator(cachingAuthenticator)
						.setAuthorizer(new LoopbackAuthorizer()).setRealm(REALM).buildAuthFilter()));
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		environment.jersey().register(RolesAllowedDynamicFeature.class);

	}

	protected void initResources(LoopbackAuthConfiguration configuration, Environment environment) {
		log.info("Registering Resources.");
		environment.jersey().register(new SecureResource());
	}

}