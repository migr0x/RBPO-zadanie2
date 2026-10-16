package org.example.factories;

import org.example.annotation.Cumponent;
import org.example.models.User;

@Cumponent
public class UserFactory {
    public User create() {
        return new User();
    }

    public User create(String name) {
        User user = new User();
        user.setName(name);
        return user;
    }
}