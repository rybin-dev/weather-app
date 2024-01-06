package com.rybindev.weatherapp.repository;

import com.rybindev.weatherapp.entity.User;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class UserRepository extends RepositoryBase<Long, User> {
    private static final String FIND_BY_EMAIL_AND_PASSWORD_HQL = "select u from User u where email =: email and password =: password";

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return getEntityManager().createQuery(FIND_BY_EMAIL_AND_PASSWORD_HQL, User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList().stream().findFirst();

    }
}
