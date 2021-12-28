package com.andhikayusup.moviex.service;

import com.andhikayusup.moviex.model.User;

import java.util.List;

public interface UserService {
    
    User saveUser(User user);
    
    User getUser(String username);
    
    List<User> getUsers();
}
