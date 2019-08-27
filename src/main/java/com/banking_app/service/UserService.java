package com.banking_app.service;



import java.util.List;

import com.banking_app.model.User;

public interface UserService {

    User save(User user);
    List<User> findAll();
    void delete(long id);
    Long userId();
}
