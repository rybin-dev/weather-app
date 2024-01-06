package com.rybindev.weatherapp.mapper;

import com.rybindev.weatherapp.dto.UserDto;
import com.rybindev.weatherapp.entity.User;

public class UserDtoMapper implements Mapper<User, UserDto> {
    @Override
    public UserDto mapFrom(User object) {
        return new UserDto(object.getId(), object.getEmail());
    }
}
