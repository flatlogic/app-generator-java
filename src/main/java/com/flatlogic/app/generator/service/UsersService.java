
package com.flatlogic.app.generator.service;

import com.flatlogic.app.generator.controller.request.UsersRequest;
import com.flatlogic.app.generator.entity.Users;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    List<Users> getUsers(Integer offset, Integer limit, String orderBy);

    List<Users> getUsers(String query, Integer limit);

    Users getUsersById(UUID id);

    Users saveUsers(UsersRequest usersRequest, String username);

    Users updateUsers(UUID id, UsersRequest usersRequest, String username);

    void deleteUsers(UUID id);

    User getUserByEmail(String email);

    void updateUserPasswordResetTokenAndSendEmail(String email);

    User updateUserPasswordByPasswordResetToken(String token, String password);

    void updateUserPassword(String username, String currentPassword, String newPassword);

}
