package com.uni.vetclinicapi.persistance.repository;

import com.uni.vetclinicapi.persistance.entity.Role;
import com.uni.vetclinicapi.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides basic CRUD operations and other needed query methods, regarding the User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Provides a User with username matching the one from the incoming String parameter.
     *
     * @param username - unique identifier for a User and parameter which needs to match if a User with specific username exists.
     * @return - an Optional of User, which either contains an Entity of type User or null.
     */
    Optional<User> findByUsername(String username);

    List<User> findAllByAuthoritiesContaining(Role role);
}
