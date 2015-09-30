package com.backof.dwloopback;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;

import io.dropwizard.Configuration;

public class LoopbackAuthConfiguration extends Configuration {

	private String logbackXmlFile;
	
	public String getLogbackXmlFile() {
		return logbackXmlFile;
	}

	public void setLogbackXmlFile(String logbackXmlFile) {
		this.logbackXmlFile = logbackXmlFile;
	}

	 @JsonProperty
    @NotEmpty
    public String mongohost = "localhost";
 
    @JsonProperty
    @Min(1)
    @Max(65535)
    public int mongoport = 27017;
 
    @JsonProperty
    @NotEmpty
    public String mongodb = "loopback";
    
    @JsonProperty
    public CacheBuilderSpec authenticationCachePolicy;
    
}
