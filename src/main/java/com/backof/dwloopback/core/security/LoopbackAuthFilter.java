package com.backof.dwloopback.core.security;

import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;

import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class LoopbackAuthFilter<P extends Principal> extends AuthFilter<AccessToken, P> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoopbackAuthFilter.class);
    private static final String LOOPBACK_SCHEME =  "Loopback Auth";
    private static final String ACCESS_TOKEN =  "access_token";
    
    private LoopbackAuthFilter() {
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
    	
        try {
        	
        	String accessToken = requestContext.getUriInfo().getQueryParameters().getFirst(ACCESS_TOKEN);
        	if (accessToken != null) {
                final AccessToken credentials = new AccessToken(accessToken);
                try {
                    final Optional<P> principal = authenticator.authenticate(credentials);
                    if (principal.isPresent()) {
                        requestContext.setSecurityContext(new SecurityContext() {
                            @Override
                            public Principal getUserPrincipal() {
                                return principal.get();
                            }

                            @Override
                            public boolean isUserInRole(String role) {
                                return authorizer.authorize(principal.get(), role);
                            }

                            @Override
                            public boolean isSecure() {
                                return requestContext.getSecurityContext().isSecure();
                            }

                            @Override
                            public String getAuthenticationScheme() {
                                return LOOPBACK_SCHEME;
                            }
                        });
                        return;
                    }
                } catch (AuthenticationException e) {
                    LOGGER.warn("Error authenticating access token", e);
                    throw new InternalServerErrorException();
                }
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Error decoding access token", e);
        }

        throw new WebApplicationException(Response.SC_UNAUTHORIZED);
    }


    /**
     * Builder for {@link LoopbackAuthFilter}.
     * <p>An {@link Authenticator} must be provided during the building process.</p>
     *
     * @param <P> the principal
     */
    public static class Builder<P extends Principal> extends
            AuthFilterBuilder<AccessToken, P, LoopbackAuthFilter<P>> {

        @Override
        protected LoopbackAuthFilter<P> newInstance() {
            return new LoopbackAuthFilter<>();
        }
    }
}