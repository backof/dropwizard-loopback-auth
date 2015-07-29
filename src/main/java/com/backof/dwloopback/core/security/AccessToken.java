package com.backof.dwloopback.core.security;

import com.google.common.base.MoreObjects;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccessToken {
	private static final String ACCESS_TOKEN =  "access_token";
	 
    private final String accessToken;
    
    public AccessToken(String token) {
        this.accessToken = checkNotNull(token);
    }

    public String getAccessToken() {
    	return accessToken;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accessToken);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AccessToken other = (AccessToken) obj;
        return Objects.equals(this.accessToken, other.accessToken);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add(ACCESS_TOKEN, accessToken)
                .toString();
    }
}