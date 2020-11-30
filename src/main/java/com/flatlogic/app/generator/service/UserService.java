package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.UserRequest;
import com.flatlogic.app.generator.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<User> getUsers(Integer offset, Integer limit, String orderBy);

    List<User> getUsers(String query, Integer limit);

    User getUserByEmail(String email);

    User getUserById(UUID id);

    User saveUser(UserRequest userRequest, String username);

    User updateUser(UUID id, UserRequest userRequest, String username);

    void updateUserPassword(String username, String currentPassword, String newPassword);

    void deleteUser(UUID id);

}
