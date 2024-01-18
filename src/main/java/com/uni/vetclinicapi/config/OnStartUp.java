package com.uni.vetclinicapi.config;

import com.uni.vetclinicapi.persistance.entity.*;
import com.uni.vetclinicapi.persistance.repository.MedicationRepository;
import com.uni.vetclinicapi.persistance.repository.PetRepository;
import com.uni.vetclinicapi.persistance.repository.UserRepository;
import com.uni.vetclinicapi.persistance.repository.VisitRepository;
import com.uni.vetclinicapi.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OnStartUp {

    private final UserRepository userRepository;

    private final PetRepository petRepository;

    private final VisitRepository visitRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final MedicationRepository medicationRepository;

    @PostConstruct
    public void InitializeTestUnits() {
        Role roleAdmin = roleService.getUserRole(Role.RoleType.ADMIN);
        User admin = new User(
                "Admin",
                passwordEncoder.encode("Password"),
                "email1@gmail.com",
                "Viktor",
                "Dimitrov",
                "0000000000",
                null,
                Set.of(roleAdmin));

        Role roleVet = roleService.getUserRole(Role.RoleType.VET);
        User vet1 = new User(
                "Vet1",
                passwordEncoder.encode("Password"),
                "email2@gmail.com",
                "Martin",
                "Petrov",
                "0000000001",
                "Surgeon",
                Set.of(roleVet));
        User vet2 = new User(
                "Vet2",
                passwordEncoder.encode("Password"),
                "email2@gmail.com",
                "Atanas",
                "Kostadinov",
                "0000000001",
                "Exotic Animal",
                Set.of(roleVet));
        User vet3 = new User(
                "Vet3",
                passwordEncoder.encode("Password"),
                "email2@gmail.com",
                "Konstantin",
                "Velichkov",
                "0000000001",
                "Companion Animal",
                Set.of(roleVet));
        User vet4 = new User(
                "Vet3",
                passwordEncoder.encode("Password"),
                "email2@gmail.com",
                "Bogomil",
                "Simeonov",
                "0000000001",
                "Surgeon",
                Set.of(roleVet));

        Role roleCustomer = roleService.getUserRole(Role.RoleType.CUSTOMER);
        User customer = new User(
                "Customer",
                passwordEncoder.encode("Password"),
                "email3@gmail.com",
                "Ivan",
                "Ivanov",
                "0000000002",
                null,
                Set.of(roleCustomer));


        Medication medication = new Medication(
                "Aspirin",
                "Tablet",
                23,
                "It just helps"
        );

        Pet pet = new Pet("Pet1","Specie1","Breed1",customer);
        Visit visit = new Visit(Date.valueOf(LocalDate.now().plusDays(1)), Time.valueOf(LocalTime.now()),true,"Test visit",pet,vet1,customer);

        medicationRepository.save(medication);
        userRepository.saveAll(List.of(admin,customer,vet1,vet2,vet3,vet4));
        petRepository.save(pet);
        visitRepository.save(visit);
        log.info("Test Entities initialized");
    }
}
