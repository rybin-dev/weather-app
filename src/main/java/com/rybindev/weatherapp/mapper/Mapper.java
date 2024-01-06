package com.rybindev.weatherapp.mapper;

public interface Mapper<F,T> {
    T mapFrom(F object);
}
