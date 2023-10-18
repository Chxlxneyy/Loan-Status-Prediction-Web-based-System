package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserRepo;

@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.example.demo.model.User user = userRepo.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not Found");
		} else {
			return new CustomUser(user);
		}
	}

}
