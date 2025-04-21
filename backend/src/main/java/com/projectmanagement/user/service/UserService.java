package com.projectmanagement.user.service;

import com.projectmanagement.user.dto.CreateUserRequest;
import com.projectmanagement.user.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO createUser(CreateUserRequest request);
    
    UserDTO getUserById(Long id);
    
    UserDTO getUserByUsername(String username);
    
    List<UserDTO> getAllUsers();
    
    UserDTO updateUser(Long id, CreateUserRequest request);
    
    void deactivateUser(Long id);
    
    void deleteUser(Long id);
} 