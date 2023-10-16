package com.uni.vetclinicapi.persistance.entity;


import jakarta.persistence.*;
import lombok.*;

/**
 * Holds the information about a given Pet.
 * Every entity gets its UUID from {@link BaseEntity}.
 * Validated through Hibernate Validator annotations.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pets")
public class Pet extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specie;

    @Column(nullable = false)
    private String breed;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;
}
