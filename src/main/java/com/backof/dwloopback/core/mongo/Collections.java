package com.backof.dwloopback.core.mongo;

public enum Collections {
	AccessToken("accessToken"),
	Role("Role"),
	RoleMapping("RoleMapping"),
	User("user");
	
	private String name;
	private Collections(String collectionName) {
		this.name=collectionName;
	}
	
	public String getName() {
		return this.name;
	}
}
