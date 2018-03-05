package jupiterpa.course.intf.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import jupiterpa.infrastructure.config.BaseSecurityConfig;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.authorizeRequests()
//			.antMatchers(HttpMethod.GET,  Controller.PATH+"/**" ).hasAnyRole("USER","ADMIN")
//			.antMatchers(HttpMethod.PUT,  Controller.PATH+"/**" ).hasRole("ADMIN")
//			.antMatchers(HttpMethod.POST, Controller.PATH+"/**" ).hasRole("ADMIN")
			.anyRequest().permitAll();
	}
}