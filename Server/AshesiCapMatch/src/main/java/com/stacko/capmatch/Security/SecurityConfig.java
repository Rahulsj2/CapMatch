package com.stacko.capmatch.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.NullRequestCache;


import com.stacko.capmatch.Configuration.AppConfig;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private AppConfig appConfig;
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	throws Exception {
		auth
			.parentAuthenticationManager(authenticationManagerBean())
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement()
				.maximumSessions(appConfig.getMaximumUserConcurrentSessions());
		
		http
			.authorizeRequests()
				.antMatchers("/loginProfiles/**", "/accountConfirmations/**", "/userPermissions/**", "/profile/**", "/users", "/faculties")
					.denyAll()				// Completely block off the above endpoints	
					
				.antMatchers("/students/browseFaculty")			// Students only end points
					.hasAnyAuthority("STUDENT", "ADMIN")
				
				.antMatchers("/faculties/browseStudents")
					.hasAnyAuthority("FACULTY", "ADMIN")
					
				.antMatchers("/students", "/students/**", "/faculties", "/faculties/**", "/users/**")
					.authenticated()
					
				.antMatchers("/login/startsession", "/logout")
					.authenticated()
					
				.antMatchers("/admin", "/admin/**", "/administrator")
					.hasAuthority("ADMIN")
					
				.antMatchers("/users/changeEmail")
					.authenticated()
				
				.antMatchers("/signup", "/signup/**", "/login", "/login/**")
					.permitAll()					
				.antMatchers("/", "/**")
					.permitAll()
				.and()
				.requestCache()
                	.requestCache(new NullRequestCache())
                .and()
				.httpBasic();			
		
		http
	      .csrf().disable();				
	}
}
