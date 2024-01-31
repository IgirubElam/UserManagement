package com.example.service;

import com.example.model.UserDtls;

public interface UserService {
	
	public UserDtls createUser(UserDtls user);
	
	public boolean checkEmail(String email);
}