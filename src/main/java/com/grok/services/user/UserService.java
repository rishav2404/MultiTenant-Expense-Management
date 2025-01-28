package com.grok.services.user;

import com.grok.entity.User;
import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer userId);

    User updateUserById(Integer userId, User tenantDetails);

    void deleteUserById(Integer userId);
}
