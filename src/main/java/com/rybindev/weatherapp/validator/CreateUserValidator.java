package com.rybindev.weatherapp.validator;

import com.rybindev.weatherapp.dto.CreateUserDto;

public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final String PATTERN_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    @Override
    public ValidationResult isValid(CreateUserDto object) {
        ValidationResult validationResult = new ValidationResult();

        if (!isValidName(object.getEmail())) {
            validationResult.add(Error.of("invalid.user.email", "Email is not valid"));
        }
        if (!isValidPassword(object.getPassword())) {
            validationResult.add(Error.of("invalid.user.password", "Password is not valid"));
        }

        return validationResult;
    }

    private boolean isValidPassword(String password) {
        return true;
    }

    private boolean isValidName(String email) {
        return email != null && email.matches(PATTERN_EMAIL);
    }
}
