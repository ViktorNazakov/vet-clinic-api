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
@Table(name = "vets")
public class Vet extends BaseEntity{

    @Column(nullable = false)
    private String fName;

    @Column(nullable = false)
    private String lName;

    @Column(nullable = false)
    private String typeOfVet;

}
