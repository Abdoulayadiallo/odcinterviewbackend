package com.odk.odcinterview.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String[] PUBLIC_MATCHERS = {"/utilisateur/login", "postulant/list","/utilisateur/register**/**","/utilisateur/role", "/utilisateur/resetPassword/**", "/image/**","/postulant/download/**"};
//	private static final String[] PUBLIC_MATCHERS = { "/**" };
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth
		.userDetailsService(userDetailsService)
		.passwordEncoder(bCryptPasswordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthentication jwtAuthentication = new JwtAuthentication(authenticationManager());
		jwtAuthentication.setFilterProcessesUrl(PUBLIC_MATCHERS[0]);
		http.csrf().disable().cors().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()	
		.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(jwtAuthentication)
		.addFilterBefore(new JwtAuthorization(), UsernamePasswordAuthenticationFilter.class);
	}
}
