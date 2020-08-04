package com.stacko.capmatch.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

@Configuration
@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
	
	@Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }	
	
	@Bean
	public HeaderHttpSessionIdResolver httpSessionStrategy() {
//		return HeaderHttpSessionIdResolver.xAuthToken();
		return new HeaderHttpSessionIdResolver("x_auth_token");
	}
	
	/**
	 * Listen for when sessions are created and destroyed to enable to control of the number of
	 * concurrent user sessions that can be active. The is set in the spring security configure() method	
	 * @return
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
	
}