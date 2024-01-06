package com.rybindev.weatherapp.service;

import com.rybindev.weatherapp.dto.CreateUserDto;
import com.rybindev.weatherapp.dto.LoginUserDto;
import com.rybindev.weatherapp.dto.UserDto;
import com.rybindev.weatherapp.entity.User;
import com.rybindev.weatherapp.exception.ValidationException;
import com.rybindev.weatherapp.mapper.UserDtoMapper;
import com.rybindev.weatherapp.mapper.UserMapper;
import com.rybindev.weatherapp.repository.UserRepository;
import com.rybindev.weatherapp.util.PasswordEncoder;
import com.rybindev.weatherapp.validator.CreateUserValidator;
import com.rybindev.weatherapp.validator.ValidationResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final CreateUserValidator createUserValidator;

    private final UserMapper userMapper;

    private final UserDtoMapper userDtoMapper;

    @Transactional
    public Long register(CreateUserDto userDto) {
        ValidationResult validationResult = createUserValidator.isValid(userDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        User user = userMapper.mapFrom(userDto);
        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    public Optional<UserDto> login(LoginUserDto loginUserDto){
        Optional<User> user = userRepository.findByEmailAndPassword(loginUserDto.getEmail(),
                PasswordEncoder.hashPassword(loginUserDto.getPassword()));

       return user.map(userDtoMapper::mapFrom);
    }

}
