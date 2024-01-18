package com.uni.vetclinicapi.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class contains the information about a given role.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    private RoleType authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }

    public enum RoleType {
        MANAGER, CUSTOMER, ADMIN, VET
    }
}