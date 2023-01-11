package com.odk.odcinterview.Service.impl;


import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur utilisateur = accountService.findByUsername(username);
		if (utilisateur == null) {
			throw new UsernameNotFoundException("Username " + username + " non trouvé");
		}
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Role userRoles = utilisateur.getRole();
		authorities.add(new SimpleGrantedAuthority(userRoles.toString()));
		return new User(utilisateur.getUsername(), utilisateur.getPassword(), authorities);
	}

}
