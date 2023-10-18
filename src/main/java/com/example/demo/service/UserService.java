package com.example.demo.service;

import java.util.List;
import com.example.demo.model.User;

public interface UserService {
	public User saveUser(User user);

	public void removeSessionMessage();
	
	List<User> getAllUsers();

	User getUsersById(Long id);

	User updateUser(User user);

	void deleteUserByID(Long id);
}
