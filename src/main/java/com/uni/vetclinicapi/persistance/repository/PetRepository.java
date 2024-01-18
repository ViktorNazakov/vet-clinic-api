package com.uni.vetclinicapi.persistance.repository;

import com.uni.vetclinicapi.persistance.entity.Pet;
import com.uni.vetclinicapi.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Provides basic CRUD operations and other needed query methods, regarding the Pet entities.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    /**
     * Provides collection of cars related to the specified user.
     *
     * @param user - the id of the specified user.
     * @return - collection of cars related to the user.
     */
    List<Pet> findAllByUser(User user);

    void deleteAllByUser(User user);
}
