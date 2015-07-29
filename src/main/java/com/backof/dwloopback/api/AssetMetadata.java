package com.backof.dwloopback.api;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AssetMetadata implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String type;
	private Long created;
	private String createdBy;
	private Long size;
	@JsonIgnore
	private String orgId;
	private String category;
	private String description;
	private List<String> tags;
	
	@JsonIgnore
	List<AssetFile> assets;
	
	public Boolean getHasThumb() {
		return hasThumb;
	}

	public void setHasThumb(Boolean hasThumb) {
		this.hasThumb = hasThumb;
	}

	private Boolean hasThumb;
	
	
	public List<AssetFile> getAssets() {
		return assets;
	}

	public void setAssets(List<AssetFile> assets) {
		this.assets = assets;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}



	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}
