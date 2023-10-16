package com.uni.vetclinicapi.service;

import com.uni.vetclinicapi.persistance.entity.Role;
import com.uni.vetclinicapi.persistance.repository.RoleRepository;
import com.uni.vetclinicapi.presentation.exceptions.RoleNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Saves all the roles that our application security needs to function properly if there aren't any in the database yet.
     */
    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            Role.RoleType[] values = Role.RoleType.values();
            for (Role.RoleType value : values) {
                roleRepository.save(new Role(value));
                log.info("Role with authority : {}, was created!", value);
            }
        }
    }

    /**
     * Fetches the role that's matching the respective authority.
     *
     * @return role that matches the authority.
     */

    public Role getUserRole(Role.RoleType authority) {
        Role role = roleRepository.findByAuthority(authority).orElseThrow(() -> new RoleNotFoundException("Role with such authority doesn't exist!"));
        log.info("Role with authority : {}, was successfully fetched!", authority);
        return role;
    }
}
