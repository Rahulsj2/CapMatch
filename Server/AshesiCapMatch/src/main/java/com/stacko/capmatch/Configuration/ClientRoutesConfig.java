package com.stacko.capmatch.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


@Component
@ConfigurationProperties(prefix="client.config.routes")
@Data
public class ClientRoutesConfig {
	
	private String basePath = "";
	
	private String resetPassword = "";
	
	private String confirmAccount = "";
	
}
