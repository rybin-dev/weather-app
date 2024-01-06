package com.rybindev.weatherapp.repository;

import com.rybindev.weatherapp.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<K extends Serializable, E extends BaseEntity<K>> {
    E save(E entity);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();

    void delete(K id);
}