package com.rybindev.weatherapp.validator;

public interface Validator<T>{
   ValidationResult isValid(T object);
}
