package com.uni.vetclinicapi.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class all the information about the given user.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@ToString
@Entity
@Builder
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 80, min = 5)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(min = 8, max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 15)
    private String fName;

    @NotBlank
    @Size(min = 3, max = 15)
    private String lName;

    @Size(min = 8, max = 15)
    private String phoneNumber;

    @Size(max = 30)
    private String vetType = null;

    // Fetch type is set to eager, cause we need to know the roles of each user at all times, they don't have many different roles, so our query won't be that slow and heavy to execute.
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> authorities = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
