package com.uni.vetclinicapi.persistance.repository;

import com.uni.vetclinicapi.persistance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides basic CRUD operations and other needed query methods, regarding the Role entities.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Provides a Role with authority matching the one from the incoming RoleType parameter.
     *
     * @param authority - an object containing the type of the authority.
     * @return - Role object with the same authority or null.
     */
    Optional<Role> findByAuthority(Role.RoleType authority);
}
