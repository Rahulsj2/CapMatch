package com.stacko.capmatch.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix="app.config")
@Data
public class AppConfig {
		
	private Integer defaultFacultyMenteeLimit = 7;
	
	private Integer studentMaxFavouriteCount = 3;
	
	private Integer facultyMaxFavouriteCount = 4;
	
	private Integer maximumUserConcurrentSessions = 2;
	
	private Integer sessionIdleLifetime = 1800;
}
