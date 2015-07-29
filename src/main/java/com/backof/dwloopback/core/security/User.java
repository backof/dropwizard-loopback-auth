package com.backof.dwloopback.core.security;


import java.security.Principal;
import java.util.List;

import com.google.common.base.MoreObjects;

public class User implements Principal {
   
	private final String accessToken;
    private String username;
    private Integer ttl;
    private Long tokenCreateDate;
    private String userId;
    private String lastName;
    private String firstName;
    private String email;
    private List<String> roles;
    
    public User(String token) {
        this.accessToken = token;
    }

    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	 @Override
	    public String toString() {
	        return MoreObjects.toStringHelper(this)
	        		.add("access token",accessToken)
	        		.add("username",username)
	        		.add("ttl",ttl)
	        		.add("token create date",tokenCreateDate)
	        		.add("user id",userId)
	        		.add("last name",lastName)
	        		.add("first name",firstName)
	        		.add("email",email)
	        		.add("roles",roles)
	                .toString();
	    }

	
	@Override
	public String getName() {
		return username;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public Integer getTtl() {
		return ttl;
	}


	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public List<String> getRoles() {
		return roles;
	}


	public void setRoles(List<String> roles) {
		this.roles = roles;
	}


	public Long getTokenCreateDate() {
		return tokenCreateDate;
	}


	public void setTokenCreateDate(Long tokenCreateDate) {
		this.tokenCreateDate = tokenCreateDate;
	}
	
}