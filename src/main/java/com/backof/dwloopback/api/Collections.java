package com.backof.dwloopback.api;

public enum Collections {
	AccessToken("accessToken"),
	Action("Action"),
	AssetFile("AssetFile"),
	LetterPackage("LetterPackage"),
	Org("Org"),
	Project("Project"),
	Record("Record"),
	Role("Role"),
	RoleMapping("RoleMapping"),
	RunConfig("RunConfig"),
	SourceData("SourceData"),
	SourceFile("SourceFile"),
	SourceSchema("SourceSchema"),
	User("user");
	
	private String name;
	private Collections(String collectionName) {
		this.name=collectionName;
	}
	
	public String getName() {
		return this.name;
	}
}
