package com.uni.vetclinicapi.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

/**
 * Holds the information about a given Medication.
 * Every entity gets its UUID from {@link BaseEntity}.
 * Validated through Hibernate Validator annotations.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "medications")
public class Medication extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    // Add Enums?
    private String type;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;
}
