package com.backof.dwloopback.api;

import java.io.Serializable;

public class Org implements Serializable {
	private static final long serialVersionUID = 1L;

	private String orgId;
	private String name;
	private String key;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
