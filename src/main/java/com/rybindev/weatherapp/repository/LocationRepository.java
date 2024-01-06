package com.rybindev.weatherapp.repository;

import com.rybindev.weatherapp.entity.Location;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class LocationRepository extends RepositoryBase<Long, Location> {
    private static final String FIND_BY_COORDINATES_HQL = "select l from Location l where latitude =: latitude and longitude =: longitude";

    public LocationRepository(EntityManager entityManager) {
        super(Location.class, entityManager);
    }

    public Optional<Location> findByCoordinates(Float latitude, Float longitude) {
        return getEntityManager().createQuery(FIND_BY_COORDINATES_HQL, Location.class)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .getResultList().stream().findFirst();

    }

}
