package com.stacko.capmatch.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/loginProfiles/**", "/accountConfirmations/**", "/userPermissions/**", "/profile/**")
					.denyAll()				// Completely block off the above endpoints	
					
				.antMatchers("/signup", "/signup/**", "/login", "login/**")
					.permitAll()
					
				.antMatchers("/students", "/students/**")
					.authenticated()
					
				.antMatchers("/faculty", "/faculty/**")
					.authenticated()
					
				.antMatchers("/admin", "/administrator")
					.hasRole("ADMIN")
					
				.antMatchers("/", "/**").permitAll()
				.and()
				.httpBasic();
			
		
		http
	      .csrf().disable();				
	}

}
