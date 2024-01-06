package com.rybindev.weatherapp.service;

import com.rybindev.weatherapp.dto.CreateUserDto;
import com.rybindev.weatherapp.entity.User;
import com.rybindev.weatherapp.exception.ValidationException;
import com.rybindev.weatherapp.mapper.UserDtoMapper;
import com.rybindev.weatherapp.mapper.UserMapper;
import com.rybindev.weatherapp.repository.UserRepository;
import com.rybindev.weatherapp.validator.CreateUserValidator;
import com.rybindev.weatherapp.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationUserServiceTest {
    private static final String EMAIL = "user@ya.ru";
    private static final String PASSWORD = "user@ya.ru";
    private static final CreateUserDto CREATE_USER_DTO = new CreateUserDto(EMAIL, PASSWORD);
    private static final User USER = User.builder()
            .id(1L)
            .email(EMAIL)
            .password(PASSWORD)
            .build();


    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CreateUserValidator userValidator;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private ValidationResult validationResult;

    @Test
    void throwExceptionIfCreateUserDtoIsInvalid() {
        doReturn(false).when(validationResult).isValid();
        doReturn(validationResult).when(userValidator).isValid(CREATE_USER_DTO);

        assertThrows(ValidationException.class, () -> userService.register(CREATE_USER_DTO));

    }

    @Test
    void register() {
        doReturn(true).when(validationResult).isValid();
        doReturn(validationResult).when(userValidator).isValid(CREATE_USER_DTO);
        doReturn(USER).when(userMapper).mapFrom(CREATE_USER_DTO);
        doReturn(USER).when(userRepository).save(USER);

        Long userId = userService.register(CREATE_USER_DTO);

        verify(userValidator).isValid(CREATE_USER_DTO);
        verify(userMapper).mapFrom(CREATE_USER_DTO);
        verify(userRepository).save(USER);

        assertThat(userId).isEqualTo(USER.getId());
    }



}