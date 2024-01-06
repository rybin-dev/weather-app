package com.rybindev.weatherapp.dto;

import lombok.Value;

@Value
public class CreateUserDto {
    String email;

    String password;
}
