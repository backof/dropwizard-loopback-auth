package com.backof.dwloopback.core.security;

import io.dropwizard.auth.Authorizer;


public class LoopbackAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        for(String userRole: user.getRoles()) {
        	if(userRole.toLowerCase().equals(role.toLowerCase())) {
        		return true;
        	}
        }
    	return false;
    }
}