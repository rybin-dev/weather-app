package com.rybindev.weatherapp.mapper;

import com.rybindev.weatherapp.dto.CreateUserDto;
import com.rybindev.weatherapp.entity.User;
import com.rybindev.weatherapp.util.PasswordEncoder;

public class UserMapper implements Mapper<CreateUserDto, User> {
    @Override
    public User mapFrom(CreateUserDto object) {
        User user = new User();
        user.setEmail(object.getEmail());
        user.setPassword(PasswordEncoder.hashPassword(object.getPassword()));
        return user;
    }

}
