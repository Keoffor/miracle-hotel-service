package com.miracle.keoffor.service;

import com.miracle.keoffor.model.User;
import com.miracle.keoffor.model.dtos.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    User registerUser (User user);

    void deleteUser(String email);

    UserDto getUser(String email);
}
